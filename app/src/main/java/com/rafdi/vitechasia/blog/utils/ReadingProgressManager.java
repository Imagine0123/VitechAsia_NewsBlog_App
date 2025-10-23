package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for managing reading progress and continue reading functionality.
 */
public class ReadingProgressManager {
    private static final String PREF_NAME = "reading_progress";
    private static final String KEY_ARTICLE_PREFIX = "article_";
    private static final String KEY_PROGRESS = "_progress";
    private static final String KEY_LAST_READ = "_last_read";
    private static final String KEY_IN_PROGRESS = "_in_progress";

    private static ReadingProgressManager instance;
    private SharedPreferences preferences;

    private ReadingProgressManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new ReadingProgressManager(context.getApplicationContext());
        }
    }

    public static ReadingProgressManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ReadingProgressManager must be initialized first");
        }
        return instance;
    }

    /**
     * Save reading progress for an article
     */
    public void saveReadingProgress(Article article, int progress) {
        if (article == null || article.getId() == null) return;

        String articleId = article.getId();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(KEY_ARTICLE_PREFIX + articleId + KEY_PROGRESS, Math.max(0, Math.min(100, progress)));
        editor.putLong(KEY_ARTICLE_PREFIX + articleId + KEY_LAST_READ, System.currentTimeMillis());
        editor.putBoolean(KEY_ARTICLE_PREFIX + articleId + KEY_IN_PROGRESS, progress > 0 && progress < 100);

        editor.apply();

        // Update the article object
        article.setReadingProgress(progress);
    }

    /**
     * Get reading progress for an article
     */
    public int getReadingProgress(String articleId) {
        if (articleId == null) return 0;
        return preferences.getInt(KEY_ARTICLE_PREFIX + articleId + KEY_PROGRESS, 0);
    }

    /**
     * Get articles that are in progress (partially read)
     */
    public List<Article> getContinueReadingArticles(List<Article> allArticles) {
        List<Article> continueReading = new ArrayList<>();

        for (Article article : allArticles) {
            if (article != null && article.getId() != null) {
                boolean isInProgress = preferences.getBoolean(
                    KEY_ARTICLE_PREFIX + article.getId() + KEY_IN_PROGRESS, false);

                if (isInProgress) {
                    // Update article with saved progress
                    int progress = getReadingProgress(article.getId());
                    article.setReadingProgress(progress);
                    continueReading.add(article);
                }
            }
        }

        // Sort by last read time (most recent first)
        continueReading.sort((a1, a2) -> {
            if (a1 == null || a2 == null) return 0;
            return Long.compare(a2.getLastReadTime(), a1.getLastReadTime());
        });

        return continueReading;
    }

    /**
     * Mark an article as completed (100% progress)
     */
    public void markArticleAsCompleted(Article article) {
        if (article == null || article.getId() == null) return;
        saveReadingProgress(article, 100);
    }

    /**
     * Clear reading progress for an article
     */
    public void clearReadingProgress(String articleId) {
        if (articleId == null) return;

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_ARTICLE_PREFIX + articleId + KEY_PROGRESS);
        editor.remove(KEY_ARTICLE_PREFIX + articleId + KEY_LAST_READ);
        editor.remove(KEY_ARTICLE_PREFIX + articleId + KEY_IN_PROGRESS);
        editor.apply();
    }

    /**
     * Add sample reading progress for testing purposes (development only)
     */
    public void addSampleProgressForTesting() {
        // This method is for development/testing only
        // Add some sample progress to demonstrate the continue reading feature

        // Get all articles and add some progress to a few of them
        // Note: In a real app, you wouldn't need this method
        // Progress would be saved naturally as users read articles

        // For now, we'll leave this empty as the user will generate progress naturally
        // by reading articles in the app
    }

    /**
     * Get reading statistics
     */
    public ReadingStats getReadingStats() {
        Map<String, ?> allPrefs = preferences.getAll();
        int totalArticlesStarted = 0;
        int totalArticlesCompleted = 0;

        for (String key : allPrefs.keySet()) {
            if (key.endsWith(KEY_PROGRESS)) {
                totalArticlesStarted++;
                int progress = preferences.getInt(key, 0);
                if (progress >= 100) {
                    totalArticlesCompleted++;
                }
            }
        }

        return new ReadingStats(totalArticlesStarted, totalArticlesCompleted);
    }

    /**
     * Simple reading statistics class
     */
    public static class ReadingStats {
        public final int totalArticlesStarted;
        public final int totalArticlesCompleted;

        public ReadingStats(int totalArticlesStarted, int totalArticlesCompleted) {
            this.totalArticlesStarted = totalArticlesStarted;
            this.totalArticlesCompleted = totalArticlesCompleted;
        }

        public double getCompletionRate() {
            if (totalArticlesStarted == 0) return 0.0;
            return (double) totalArticlesCompleted / totalArticlesStarted * 100.0;
        }
    }
}
