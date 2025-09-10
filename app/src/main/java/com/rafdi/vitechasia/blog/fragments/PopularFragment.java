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

public class PopularFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> articleList;
    
    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        
        // Initialize RecyclerView with vertical layout
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize article list and adapter
        articleList = new ArrayList<>();
        articleVerticalAdapter = new ArticleVerticalAdapter(articleList, this::onArticleClick);
        recyclerView.setAdapter(articleVerticalAdapter);
        
        // Load popular articles
        loadPopularArticles();
        
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
    
    private void loadPopularArticles() {
        // This is sample data - replace with your actual data loading logic
        List<Article> popularArticles = new ArrayList<>();
        
        // Technology Category
        popularArticles.add(createSampleArticle(
                "The Future of Artificial Intelligence",
                "AI Researcher",
                "https://example.com/ai-future.jpg",
                "Exploring the latest advancements in AI and how they're shaping our future. From healthcare to transportation, AI is revolutionizing industries worldwide.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
                350,
                "Technology",
                "Artificial Intelligence"
        ));
        
        // Health & Fitness Category
        popularArticles.add(createSampleArticle(
                "10-Minute Workouts for Busy Professionals",
                "Fitness Coach",
                "https://example.com/quick-workouts.jpg",
                "Short on time? These effective 10-minute workouts can be done anywhere and require no equipment. Perfect for busy professionals looking to stay fit.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 36, // 36 hours ago
                280,
                "Health & Wellness",
                "Fitness"
        ));
        
        // Food & Cooking Category
        popularArticles.add(createSampleArticle(
                "5 Easy Vegan Recipes for Beginners",
                "Chef Maria",
                "https://example.com/vegan-recipes.jpg",
                "Delicious and simple vegan recipes that even meat-lovers will enjoy. These dishes are packed with flavor and nutrition, perfect for anyone looking to incorporate more plant-based meals.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
                195,
                "Food & Cooking",
                "Vegan Recipes"
        ));
        
        // Travel Category
        popularArticles.add(createSampleArticle(
                "Hidden Gems: Unexplored European Destinations",
                "Travel Enthusiast",
                "https://example.com/european-gems.jpg",
                "Escape the tourist crowds with our guide to Europe's best-kept secrets. From charming villages to breathtaking landscapes, discover destinations off the beaten path.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3, // 3 days ago
                420,
                "Travel",
                "Europe"
        ));
        
        // Personal Finance Category
        popularArticles.add(createSampleArticle(
                "Smart Investment Strategies for 2025",
                "Financial Advisor",
                "https://example.com/investment-strategies.jpg",
                "Navigate the complex world of investments with these expert strategies. Learn how to build a diversified portfolio and make informed financial decisions.",
                System.currentTimeMillis() - 1000 * 60 * 60 * 60, // 60 hours ago
                310,
                "Finance",
                "Investing"
        ));
        
        // Update the adapter with the new data
        articleList.clear();
        articleList.addAll(popularArticles);
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
