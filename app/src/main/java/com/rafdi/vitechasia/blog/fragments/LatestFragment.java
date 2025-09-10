package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class LatestFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> articleList;
    
    public LatestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        
        // Initialize RecyclerView with vertical layout
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize article list and adapter
        articleList = new ArrayList<>();
        articleVerticalAdapter = new ArticleVerticalAdapter(articleList, this::onArticleClick);
        recyclerView.setAdapter(articleVerticalAdapter);
        
        // Load latest articles
        loadLatestArticles();
        
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
    
    private void loadLatestArticles() {
        // This is sample data - replace with your actual data loading logic
        List<Article> latestArticles = new ArrayList<>();
        
        // Add sample articles with detailed content
        latestArticles.add(createSampleArticle(
                "Latest Tech Trends 2025",
                "Tech Analyst",
                "https://example.com/tech-trends-2025.jpg",
                "The technology landscape in 2025 is seeing unprecedented growth in AI integration across all sectors. From smart homes to autonomous vehicles, the way we interact with technology is evolving rapidly. This article explores the top trends shaping our digital future.",
                System.currentTimeMillis() - 1000 * 60 * 30, // 30 minutes ago
                85,
                "tech",
                "trends"
        ));
        
        latestArticles.add(createSampleArticle(
                "Sustainable Living: A Complete Guide",
                "Eco Warrior",
                "https://example.com/sustainable-living.jpg",
                "Discover practical tips and innovative solutions for reducing your carbon footprint. This comprehensive guide covers everything from zero-waste living to renewable energy options for your home.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
                120,
                "lifestyle",
                "sustainability"
        ));
        
        latestArticles.add(createSampleArticle(
                "The Future of Remote Work",
                "Workplace Expert",
                "https://example.com/remote-work-future.jpg",
                "As remote work becomes the new norm, companies are reimagining their work policies. This article examines the long-term impacts of remote work on productivity, company culture, and urban development.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 5, // 5 hours ago
                95,
                "business",
                "remote-work"
        ));
        
        // Update the adapter with the new data
        articleList.clear();
        articleList.addAll(latestArticles);
        articleVerticalAdapter.notifyDataSetChanged();
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
