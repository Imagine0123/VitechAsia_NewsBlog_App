package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing search history.
 * Stores and retrieves user's recent search queries using SharedPreferences.
 */
public class SearchHistoryManager {
    private static final String PREF_NAME = "SearchHistoryPreferences";
    private static final String KEY_SEARCH_HISTORY = "search_history";
    private static final int MAX_HISTORY_SIZE = 5;
    private final Gson gson = new Gson();

    private static SearchHistoryManager instance;
    private SharedPreferences preferences;

    private SearchHistoryManager(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SearchHistoryManager getInstance(Context context) {
        if (instance == null) {
            instance = new SearchHistoryManager(context);
        }
        return instance;
    }

    /**
     * Add a search query to history
     */
    public void addSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }

        List<String> historyList = getSearchHistory();

        // Remove if already exists (to move to top)
        historyList.remove(query.trim());

        // Add to beginning
        historyList.add(0, query.trim());

        // Keep only latest searches
        if (historyList.size() > MAX_HISTORY_SIZE) {
            historyList = historyList.subList(0, MAX_HISTORY_SIZE);
        }

        // Save back to preferences
        saveHistory(historyList);
    }

    /**
     * Get search history as a list
     */
    @SuppressWarnings("unchecked")
    public List<String> getSearchHistory() {
        try {
            // First try to get as JSON string (new format)
            if (preferences.contains(KEY_SEARCH_HISTORY)) {
                try {
                    String historyJson = preferences.getString(KEY_SEARCH_HISTORY, null);
                    if (historyJson != null && !historyJson.isEmpty()) {
                        // Try to parse as JSON array
                        Type type = new TypeToken<List<String>>() {}.getType();
                        List<String> history = gson.fromJson(historyJson, type);
                        if (history != null) {
                            return history;
                        }
                    }
                } catch (ClassCastException e) {
                    // This might be the old format, try to migrate
                    try {
                        // Check if it's a Set<String>
                        if (preferences.contains(KEY_SEARCH_HISTORY)) {
                            Object value = preferences.getAll().get(KEY_SEARCH_HISTORY);
                            if (value instanceof Set) {
                                Set<String> oldSet = (Set<String>) value;
                                if (!oldSet.isEmpty()) {
                                    // Convert to list and save in new format
                                    List<String> migratedList = new ArrayList<>(oldSet);
                                    // Save in new format
                                    saveHistory(migratedList);
                                    // Clear old format
                                    preferences.edit().remove(KEY_SEARCH_HISTORY).apply();
                                    return migratedList;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // If we get here, return empty list
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private void saveHistory(List<String> history) {
        String historyJson = gson.toJson(history);
        preferences.edit().putString(KEY_SEARCH_HISTORY, historyJson).apply();
    }

    /**
     * Clear all search history
     */
    public void clearSearchHistory() {
        preferences.edit().remove(KEY_SEARCH_HISTORY).apply();
    }

    /**
     * Remove a specific search query from history
     */
    public void removeSearchQuery(String query) {
        if (query == null) return;

        List<String> history = getSearchHistory();
        history.remove(query.trim());

        // Save updated history
        saveHistory(history);
    }

    /**
     * Check if search history is empty
     */
    public boolean isHistoryEmpty() {
        return getHistory().isEmpty();
    }
    
    private List<String> getHistory() {
        List<String> history = getSearchHistory();
        return history != null ? history : new ArrayList<>();
    }
}
