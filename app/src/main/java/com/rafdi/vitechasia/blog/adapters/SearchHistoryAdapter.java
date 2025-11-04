package com.rafdi.vitechasia.blog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Adapter for displaying search history suggestions in a dropdown.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {

    private final List<String> searchHistory;
    private final OnSearchSuggestionClickListener listener;

    public interface OnSearchSuggestionClickListener {
        void onSearchSuggestionClick(String query);
    }

    public SearchHistoryAdapter(List<String> searchHistory, OnSearchSuggestionClickListener listener) {
        this.searchHistory = new ArrayList<>(searchHistory != null ? searchHistory : new ArrayList<>());
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_suggestion, parent, false);
        return new SearchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        String query = searchHistory.get(position);
        holder.bind(query);
    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    /**
     * Updates the search history list with efficient diffing
     * @param newHistory New list of search history items
     */
    public void updateSearchHistory(List<String> newHistory) {
        if (newHistory == null) {
            newHistory = new ArrayList<>();
        }
        
        SearchHistoryDiffCallback diffCallback = new SearchHistoryDiffCallback(this.searchHistory, newHistory);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        
        this.searchHistory.clear();
        this.searchHistory.addAll(newHistory);
        
        diffResult.dispatchUpdatesTo(this);
    }
    
    /**
     * Callback for calculating the diff between old and new search history lists
     */
    private static class SearchHistoryDiffCallback extends DiffUtil.Callback {
        private final List<String> oldList;
        private final List<String> newList;
        
        public SearchHistoryDiffCallback(List<String> oldList, List<String> newList) {
            this.oldList = oldList != null ? oldList : new ArrayList<>();
            this.newList = newList != null ? newList : new ArrayList<>();
        }
        
        @Override
        public int getOldListSize() {
            return oldList.size();
        }
        
        @Override
        public int getNewListSize() {
            return newList.size();
        }
        
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // For search history, we consider items the same if they have the same string value
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
        
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // For search history, the content is the same if the strings are equal
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
        
        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // No payload needed for search history items
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView searchQueryText;

        SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            searchQueryText = itemView.findViewById(R.id.searchQueryText);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onSearchSuggestionClick(searchHistory.get(position));
                }
            });
        }

        void bind(String query) {
            searchQueryText.setText(query);
        }
    }
}
