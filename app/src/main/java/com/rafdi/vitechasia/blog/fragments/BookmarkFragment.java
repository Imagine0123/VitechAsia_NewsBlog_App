package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BookmarkFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> bookmarkedArticles;
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
        emptyView = view.findViewById(R.id.empty_view);
        
        // Setup RecyclerView with vertical layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize adapter
        bookmarkedArticles = new ArrayList<>();
        articleVerticalAdapter = new ArticleVerticalAdapter(bookmarkedArticles, this::onArticleClick);
        recyclerView.setAdapter(articleVerticalAdapter);
        
        // Load bookmarked articles
        loadBookmarkedArticles();
        
        return view;
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
    
    private void loadBookmarkedArticles() {
        // This is sample data - replace with your actual bookmarks loading logic
        List<Article> bookmarks = new ArrayList<>();
        
        // Education Category
        bookmarks.add(createSampleArticle(
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
        bookmarks.add(createSampleArticle(
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
        bookmarks.add(createSampleArticle(
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
        bookmarks.add(createSampleArticle(
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
        bookmarks.add(createSampleArticle(
                "Mastering Time Management",
                "Productivity Coach",
                "https://example.com/time-management.jpg",
                "Effective strategies to take control of your schedule and boost productivity. Learn how to prioritize tasks, eliminate time-wasters, and achieve more in less time.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 5, // 5 days ago
                230,
                "Personal Development",
                "Productivity"
        ));
        
        // Update the adapter with the new data
        bookmarkedArticles.clear();
        bookmarkedArticles.addAll(bookmarks);
        articleVerticalAdapter.notifyDataSetChanged();
        
        // Show empty view if no bookmarks
        if (bookmarks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
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
