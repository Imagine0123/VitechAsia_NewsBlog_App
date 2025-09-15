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
import com.rafdi.vitechasia.blog.utils.PaginationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BookmarkFragment extends Fragment {
    
    private static final int ITEMS_PER_PAGE = 10; // 10 items per page
    
    private RecyclerView recyclerView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> bookmarkedArticles;
    private List<Article> allBookmarkedArticles;
    private TextView emptyView;
    private PaginationUtils<Article> paginationUtils;
    
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
        
        // Setup RecyclerView with vertical layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize article lists
        allBookmarkedArticles = new ArrayList<>();
        bookmarkedArticles = new ArrayList<>();
        
        // Initialize adapter
        articleVerticalAdapter = new ArticleVerticalAdapter(bookmarkedArticles, this::onArticleClick);
        recyclerView.setAdapter(articleVerticalAdapter);
        
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
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // This is sample data - replace with your actual bookmarks loading logic
            allBookmarkedArticles.clear();
            
            // Education Category
            allBookmarkedArticles.add(createSampleArticle(
                    "The Future of Online Learning",
                    "Education Specialist",
                    "https://example.com/online-learning.jpg",
                    "How digital platforms are transforming education and making learning more accessible. Explore the latest trends in e-learning and how they're shaping the future of education.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 12, // 12 hours ago
                    275,
                    "Education",
                    "E-Learning"
            ));
            
            // Technology Category
            allBookmarkedArticles.add(createSampleArticle(
                    "Cybersecurity Best Practices in 2025",
                    "Security Expert",
                    "https://example.com/cybersecurity.jpg",
                    "Essential cybersecurity measures everyone should know to protect their digital life. Learn about the latest threats and how to safeguard your personal and professional data.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2, // 2 days ago
                    320,
                    "Technology",
                    "Cybersecurity"
            ));
            
            // Health & Wellness Category
            allBookmarkedArticles.add(createSampleArticle(
                    "The Science of Better Sleep",
                    "Sleep Scientist",
                    "https://example.com/sleep-science.jpg",
                    "Discover evidence-based strategies to improve your sleep quality and wake up refreshed. Learn about sleep cycles, optimal sleep environments, and habits for better rest.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 72, // 3 days ago
                    410,
                    "Health & Wellness",
                    "Sleep Health"
            ));
            
            // Environment Category
            allBookmarkedArticles.add(createSampleArticle(
                    "Urban Gardening: Grow Your Own Food",
                    "Urban Farmer",
                    "https://example.com/urban-gardening.jpg",
                    "Even with limited space, you can grow fresh produce at home. This guide covers everything from container gardening to vertical farming techniques for urban dwellers.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 4, // 4 days ago
                    185,
                    "Environment",
                    "Sustainable Living"
            ));
            
            // Personal Development Category
            allBookmarkedArticles.add(createSampleArticle(
                    "Mastering Time Management",
                    "Productivity Coach",
                    "https://example.com/time-management.jpg",
                    "Effective strategies to take control of your schedule and boost productivity. Learn how to prioritize tasks, eliminate time-wasters, and achieve more in less time.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 5, // 5 days ago
                    230,
                    "Personal Development",
                    "Productivity"
            ));
            
            // Initialize pagination
            paginationUtils = new PaginationUtils<>(allBookmarkedArticles, ITEMS_PER_PAGE);
            
            // Load first page
            loadPage();
            
            // Update empty view visibility
            if (allBookmarkedArticles.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                btnLoadMore.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            
            // Hide loading
            progressBar.setVisibility(View.GONE);
        }, 1000); // Simulate network delay
    }
    
    private void loadPage() {
        if (paginationUtils == null) return;
        
        // Get current page items
        List<Article> pageItems = paginationUtils.getCurrentPageItems();
        
        // Update UI based on current page
        if (paginationUtils.getCurrentPage() == 1) {
            // First page, replace all items
            bookmarkedArticles.clear();
            bookmarkedArticles.addAll(pageItems);
        } else {
            // Subsequent pages, append items
            bookmarkedArticles.addAll(pageItems);
        }
        
        // Notify adapter
        articleVerticalAdapter.notifyDataSetChanged();
        
        // Show/hide load more button
        btnLoadMore.setVisibility(paginationUtils.hasNextPage() ? View.VISIBLE : View.GONE);
    }
    
    private void loadNextPage() {
        if (paginationUtils == null || !paginationUtils.hasNextPage()) {
            btnLoadMore.setVisibility(View.GONE);
            return;
        }
        
        // Show loading
        btnLoadMore.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // Load next page
            paginationUtils.nextPage();
            loadPage();
            
            // Hide loading
            progressBar.setVisibility(View.GONE);
        }, 500); // Simulate network delay
    }
    
    private void onArticleClick(Article article) {
        // Navigate to article detail
        ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(article);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, articleDetailFragment)
                .addToBackStack(null)
                .commit();
    }
    
    // Helper method to create sample articles
    private Article createSampleArticle(String title, String authorName, String imageUrl, 
                                      String content, long timestamp, int viewCount,
                                      String categoryId, String subcategoryId) {
        Article article = new Article();
        article.setId(UUID.randomUUID().toString());
        article.setTitle(title);
        article.setContent(content);
        article.setImageUrl(imageUrl);
        article.setCategory(categoryId);
        article.setCategoryId(categoryId);
        article.setSubcategoryId(subcategoryId);
        article.setAuthorName(authorName);
        article.setPublishDate(new Date(timestamp));
        article.setViewCount(viewCount);
        return article;
    }
}
