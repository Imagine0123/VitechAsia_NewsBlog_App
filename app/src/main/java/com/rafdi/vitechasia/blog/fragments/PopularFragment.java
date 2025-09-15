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
import com.rafdi.vitechasia.blog.utils.PaginationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PopularFragment extends Fragment {
    
    private static final int ITEMS_PER_PAGE = 5; // 5 items per page
    
    private RecyclerView recyclerView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> articleList;
    private List<Article> allArticles;
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
        
        // Initialize RecyclerView with vertical layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize article lists
        allArticles = new ArrayList<>();
        articleList = new ArrayList<>();
        
        // Initialize adapter
        articleVerticalAdapter = new ArticleVerticalAdapter(articleList, this::onArticleClick);
        recyclerView.setAdapter(articleVerticalAdapter);
        
        // Set up load more button
        btnLoadMore.setOnClickListener(v -> loadNextPage());
        
        // Load popular articles
        loadPopularArticles();
        
        return view;
    }
    
    private void loadPopularArticles() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // Clear existing articles
            allArticles.clear();
            
            // Add 20 sample articles (4 pages of 5 items each)
            
            // Page 1 - Most Popular (highest view counts)
            allArticles.add(createSampleArticle(
                    "10-Minute Workouts for Busy Professionals",
                    "Fitness Coach",
                    "https://example.com/quick-workouts.jpg",
                    "Short on time? These effective 10-minute workouts can be done anywhere and require no equipment. Perfect for busy professionals looking to stay fit.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
                    2890,
                    "Health & Wellness",
                    "Fitness"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Artificial Intelligence",
                    "AI Researcher",
                    "https://example.com/ai-future.jpg",
                    "Exploring the latest advancements in AI and how they're shaping our future. From healthcare to transportation, AI is revolutionizing industries worldwide.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 36, // 1.5 days ago
                    2675,
                    "Technology",
                    "Artificial Intelligence"
            ));
            
            allArticles.add(createSampleArticle(
                    "5 Easy Vegan Recipes for Beginners",
                    "Chef Maria",
                    "https://example.com/vegan-recipes.jpg",
                    "Delicious and simple vegan recipes that even meat-lovers will enjoy. These dishes are packed with flavor and nutrition, perfect for anyone looking to incorporate more plant-based meals.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
                    2450,
                    "Food & Cooking",
                    "Vegan Recipes"
            ));
            
            allArticles.add(createSampleArticle(
                    "Hidden Gems: Unexplored European Destinations",
                    "Travel Enthusiast",
                    "https://example.com/european-gems.jpg",
                    "Escape the tourist crowds with our guide to Europe's best-kept secrets. From charming villages to breathtaking landscapes, discover destinations off the beaten path.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 60, // 2.5 days ago
                    2310,
                    "Travel",
                    "Europe"
            ));
            
            allArticles.add(createSampleArticle(
                    "Smart Investment Strategies for 2025",
                    "Financial Advisor",
                    "https://example.com/investment-strategies.jpg",
                    "Navigate the complex world of investments with these expert strategies. Learn how to build a diversified portfolio and make informed financial decisions.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 72, // 3 days ago
                    2190,
                    "Finance",
                    "Investing"
            ));
            
            // Page 2 - Still Popular
            allArticles.add(createSampleArticle(
                    "The Science of Intermittent Fasting",
                    "Nutrition Expert",
                    "https://example.com/intermittent-fasting.jpg",
                    "How intermittent fasting can improve your health and help with weight management. Learn about different fasting methods and their benefits.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 84, // 3.5 days ago
                    1980,
                    "Health & Wellness",
                    "Nutrition"
            ));
            
            allArticles.add(createSampleArticle(
                    "Blockchain Technology Explained",
                    "Crypto Analyst",
                    "https://example.com/blockchain-explained.jpg",
                    "A beginner's guide to understanding blockchain technology and its potential applications beyond cryptocurrency.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 96, // 4 days ago
                    1875,
                    "Technology",
                    "Blockchain"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Ultimate Guide to Minimalism",
                    "Lifestyle Coach",
                    "https://example.com/minimalism-guide.jpg",
                    "How to embrace minimalism and simplify your life. Practical tips for decluttering your space and focusing on what truly matters.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 108, // 4.5 days ago
                    1760,
                    "Lifestyle",
                    "Minimalism"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Electric Vehicles",
                    "Auto Expert",
                    "https://example.com/ev-future.jpg",
                    "What to expect from the next generation of electric cars, from longer ranges to faster charging and autonomous features.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 120, // 5 days ago
                    1650,
                    "Automotive",
                    "Electric Vehicles"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Art of Public Speaking",
                    "Communication Coach",
                    "https://example.com/public-speaking.jpg",
                    "Overcome your fear of public speaking with these proven techniques. Learn how to engage your audience and deliver powerful presentations.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 132, // 5.5 days ago
                    1540,
                    "Personal Development",
                    "Communication"
            ));
            
            // Page 3 - Trending
            allArticles.add(createSampleArticle(
                    "The Psychology of Color in Marketing",
                    "Marketing Specialist",
                    "https://example.com/color-psychology.jpg",
                    "How different colors influence consumer behavior and how to use them effectively in your marketing strategy.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 144, // 6 days ago
                    1430,
                    "Marketing",
                    "Consumer Psychology"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Science of Sleep",
                    "Sleep Researcher",
                    "https://example.com/sleep-science.jpg",
                    "Understanding the importance of quality sleep and how it affects your physical and mental health. Tips for better sleep hygiene.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 156, // 6.5 days ago
                    1320,
                    "Health & Wellness",
                    "Sleep"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Rise of Remote Work",
                    "Workplace Strategist",
                    "https://example.com/remote-work-rise.jpg",
                    "How remote work is changing the way we think about the workplace. Benefits, challenges, and the future of flexible work arrangements.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 168, // 7 days ago
                    1210,
                    "Business",
                    "Remote Work"
            ));
            
            allArticles.add(createSampleArticle(
                    "Sustainable Fashion: A Complete Guide",
                    "Fashion Activist",
                    "https://example.com/sustainable-fashion-guide.jpg",
                    "How to build an eco-friendly wardrobe and support ethical fashion brands that prioritize sustainability.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 180, // 7.5 days ago
                    1100,
                    "Fashion",
                    "Sustainable Fashion"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Space Exploration",
                    "Space Enthusiast",
                    "https://example.com/space-future.jpg",
                    "What's next in space exploration? From Mars missions to space tourism, discover the exciting developments in the final frontier.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 192, // 8 days ago
                    990,
                    "Science",
                    "Space Exploration"
            ));
            
            // Page 4 - Still Trending
            allArticles.add(createSampleArticle(
                    "The Benefits of Meditation",
                    "Mindfulness Coach",
                    "https://example.com/meditation-benefits.jpg",
                    "How regular meditation can improve your mental and physical health. Simple techniques to get started with a meditation practice.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 204, // 8.5 days ago
                    880,
                    "Health & Wellness",
                    "Meditation"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Art of Negotiation",
                    "Business Consultant",
                    "https://example.com/negotiation.jpg",
                    "Master the art of negotiation with these proven strategies for getting what you want in business and in life.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 216, // 9 days ago
                    770,
                    "Business",
                    "Negotiation"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Renewable Energy",
                    "Energy Analyst",
                    "https://example.com/renewable-future.jpg",
                    "How renewable energy is transforming the global energy landscape and what it means for the future of our planet.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 228, // 9.5 days ago
                    660,
                    "Science & Environment",
                    "Renewable Energy"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Psychology of Habits",
                    "Behavioral Scientist",
                    "https://example.com/habit-psychology.jpg",
                    "Understanding how habits are formed and how to change them. Science-backed strategies for building good habits and breaking bad ones.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 240, // 10 days ago
                    550,
                    "Psychology",
                    "Habit Formation"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Social Media",
                    "Digital Marketer",
                    "https://example.com/social-media-future.jpg",
                    "Emerging trends in social media and how they're changing the way we connect and consume content online.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 252, // 10.5 days ago
                    440,
                    "Technology",
                    "Social Media"
            ));
            
            // Initialize pagination with 5 items per page
            paginationUtils = new PaginationUtils<>(allArticles, ITEMS_PER_PAGE);
            
            // Load first page
            loadPage();
            
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
            articleList.clear();
            articleList.addAll(pageItems);
        } else {
            // Subsequent pages, append items
            articleList.addAll(pageItems);
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
