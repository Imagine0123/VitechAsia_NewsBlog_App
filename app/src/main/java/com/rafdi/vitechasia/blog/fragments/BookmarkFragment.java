package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DataHandler;
import com.rafdi.vitechasia.blog.utils.PaginationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying user's bookmarked articles.
 * Supports pagination and bookmark management.
 */
public class BookmarkFragment extends Fragment {
    
    private static final int ITEMS_PER_PAGE = 10;
    
    private RecyclerView recyclerView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private ArticleVerticalAdapter verticalAdapter;
    private PaginationUtils<Article> paginationUtils;
    private TextView emptyView;
    
    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        btnLoadMore = view.findViewById(R.id.btn_load_more);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyView = view.findViewById(R.id.empty_view);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize adapter
        verticalAdapter = new ArticleVerticalAdapter(new ArrayList<>(), this::onArticleClick);
        recyclerView.setAdapter(verticalAdapter);
        
        // Set up load more button
        btnLoadMore.setOnClickListener(v -> loadNextPage());
        
        // Load bookmarked articles
        loadBookmarkedArticles();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh bookmarks when returning to this fragment
        loadBookmarkedArticles();
    }
    
    private void loadBookmarkedArticles() {
        showLoading(true);
        emptyView.setVisibility(View.GONE);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // Get bookmarked articles from DummyDataGenerator
            List<Article> bookmarkedArticles = DataHandler.getBookmarkedArticles(requireContext());
            
            // Initialize or update pagination
            if (paginationUtils == null) {
                paginationUtils = new PaginationUtils<>(bookmarkedArticles, ITEMS_PER_PAGE);
            } else {
                paginationUtils.updateData(bookmarkedArticles);
            }
            
            // Update UI
            updateArticleList();
            showLoading(false);
            updateEmptyState();
            updateLoadMoreButton();
            
        }, 500); // Simulate network delay
    }
    
    private void updateArticleList() {
        if (paginationUtils != null) {
            List<Article> currentPage = paginationUtils.getCurrentPageItems();
            verticalAdapter.setArticles(currentPage);
        }
    }
    
    private void updateEmptyState() {
        if (verticalAdapter != null) {
            if (verticalAdapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void updateLoadMoreButton() {
        if (paginationUtils != null) {
            btnLoadMore.setVisibility(paginationUtils.hasNextPage() ? View.VISIBLE : View.GONE);
        } else {
            btnLoadMore.setVisibility(View.GONE);
        }
    }
    
    private void loadNextPage() {
        if (paginationUtils != null && paginationUtils.hasNextPage()) {
            paginationUtils.loadNextPage();
            updateArticleList();
            updateLoadMoreButton();
        }
    }
    
    private void onArticleClick(Article article) {
        // Navigate to article detail
        ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(article);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, articleDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    
    private void showLoading(boolean show) {
        if (getView() != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
