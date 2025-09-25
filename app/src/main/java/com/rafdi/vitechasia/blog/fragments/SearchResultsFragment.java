package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.activities.HomePage;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment implements ArticleVerticalAdapter.OnArticleClickListener {
    private static final String ARG_QUERY = "search_query";

    private String searchQuery;
    private RecyclerView resultsRecyclerView;
    private ArticleVerticalAdapter adapter;
    private TextView noResultsText;

    public static SearchResultsFragment newInstance(String query) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery = getArguments().getString(ARG_QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        // Initialize views
        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView);
        noResultsText = view.findViewById(R.id.noResultsText);

        // Setup RecyclerView with this fragment as the click listener
        adapter = new ArticleVerticalAdapter(new ArrayList<>(), this);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        resultsRecyclerView.setAdapter(adapter);

        // Perform search
        performSearch();

        return view;
    }

    @Override
    public void onArticleClick(Article article) {
        // Handle article click by delegating to parent activity if it's HomePage
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).loadArticleDetailFragment(article);
        }
    }

    private void performSearch() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            showNoResults();
            return;
        }

        try {
            // Perform search using DummyDataGenerator
            List<Article> searchResults = DummyDataGenerator.searchArticles(searchQuery);

            // Update UI on the main thread
            requireActivity().runOnUiThread(() -> {
                if (searchResults == null || searchResults.isEmpty()) {
                    showNoResults();
                } else {
                    showResults(searchResults);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            requireActivity().runOnUiThread(this::showNoResults);
        }
    }

    private void showResults(List<Article> results) {
        if (resultsRecyclerView != null && noResultsText != null) {
            resultsRecyclerView.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);
            adapter.setArticles(results);
        }
    }

    private void showNoResults() {
        if (resultsRecyclerView != null && noResultsText != null) {
            resultsRecyclerView.setVisibility(View.GONE);
            noResultsText.setVisibility(View.VISIBLE);
            noResultsText.setText(getString(R.string.no_search_results, searchQuery != null ? searchQuery : ""));
        }
    }
}
