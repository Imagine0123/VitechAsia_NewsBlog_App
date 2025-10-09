package com.rafdi.vitechasia.blog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;

import java.util.List;

/**
 * Adapter for displaying search history suggestions in a dropdown.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {

    private List<String> searchHistory;
    private OnSearchSuggestionClickListener listener;

    public interface OnSearchSuggestionClickListener {
        void onSearchSuggestionClick(String query);
    }

    public SearchHistoryAdapter(List<String> searchHistory, OnSearchSuggestionClickListener listener) {
        this.searchHistory = searchHistory;
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

    public void updateSearchHistory(List<String> newHistory) {
        this.searchHistory = newHistory;
        notifyDataSetChanged();
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
