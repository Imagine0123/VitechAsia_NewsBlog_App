package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

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
 * Fragment for displaying most popular articles based on view count.
 * Supports pagination for loading more trending content.
 */
public class PopularFragment extends Fragment {
    
    private static final int ITEMS_PER_PAGE = 10;
    
    private RecyclerView recyclerView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private ArticleVerticalAdapter verticalAdapter;
    private PaginationUtils<Article> paginationUtils;
    
    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        btnLoadMore = view.findViewById(R.id.btn_load_more);
        progressBar = view.findViewById(R.id.progress_bar);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize adapter
        verticalAdapter = new ArticleVerticalAdapter(new ArrayList<>(), this::onArticleClick);
        recyclerView.setAdapter(verticalAdapter);
        
        // Set up load more button
        btnLoadMore.setOnClickListener(v -> loadNextPage());
        
        // Load popular articles
        loadPopularArticles();
        
        return view;
    }
    
    private void loadPopularArticles() {
        showLoading(true);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // Get most viewed articles directly from DummyDataGenerator
            List<Article> popularArticles = DataHandler.getMostViewedArticles(100); // Get top 100 most viewed articles
            
            // Initialize or update pagination
            if (paginationUtils == null) {
                paginationUtils = new PaginationUtils<>(popularArticles, ITEMS_PER_PAGE);
            } else {
                paginationUtils.updateData(popularArticles);
            }
            
            // Update UI
            updateArticleList();
            showLoading(false);
            updateLoadMoreButton();
            
        }, 500); // Simulate network delay
    }
    
    private void updateArticleList() {
        if (paginationUtils != null) {
            List<Article> currentPage = paginationUtils.getCurrentPageItems();
            verticalAdapter.setArticles(currentPage);
        }
    }
    
    private void updateLoadMoreButton() {
        if (getView() != null && paginationUtils != null) {
            btnLoadMore.setVisibility(paginationUtils.hasNextPage() ? View.VISIBLE : View.GONE);
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
