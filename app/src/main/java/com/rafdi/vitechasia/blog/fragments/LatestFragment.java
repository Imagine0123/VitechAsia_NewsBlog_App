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
import java.util.List;

public class LatestFragment extends Fragment {
    
    private static final int ITEMS_PER_PAGE = 5; // 5 items per page as requested
    
    private RecyclerView recyclerView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private ArticleVerticalAdapter articleVerticalAdapter;
    private List<Article> articleList;
    private List<Article> allArticles;
    private PaginationUtils<Article> paginationUtils;
    
    public LatestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        
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
        
        // Load latest articles
        loadLatestArticles();
        
        return view;
    }
    
    private void loadLatestArticles() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Simulate network/database call
        new android.os.Handler().postDelayed(() -> {
            // Clear existing articles
            allArticles.clear();
            
            // Add 20 sample articles (4 pages of 5 items each)
            
            // Page 1
            allArticles.add(createSampleArticle(
                    "The Future of AI in Healthcare",
                    "Dr. Sarah Chen",
                    "https://example.com/ai-healthcare.jpg",
                    "How artificial intelligence is revolutionizing healthcare with faster diagnostics and personalized treatment plans. AI is helping doctors make better decisions and improving patient outcomes.",
                    System.currentTimeMillis() - 1000 * 60 * 30, // 30 minutes ago
                    245,
                    "Technology",
                    "Artificial Intelligence"
            ));
            
            allArticles.add(createSampleArticle(
                    "Sustainable Travel in 2025",
                    "Eco Traveler",
                    "https://example.com/sustainable-travel.jpg",
                    "Discover the best eco-friendly destinations and travel tips to reduce your carbon footprint while exploring the world. Learn about carbon offset programs and sustainable accommodations.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
                    189,
                    "Travel",
                    "Eco Tourism"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Rise of Remote Work",
                    "Workplace Analyst",
                    "https://example.com/remote-work.jpg",
                    "How remote work is changing company culture and employee expectations. Explore the benefits and challenges of distributed teams and the future of the workplace.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 5, // 5 hours ago
                    312,
                    "Business",
                    "Workplace Trends"
            ));
            
            allArticles.add(createSampleArticle(
                    "Mindfulness Meditation Guide",
                    "Wellness Expert",
                    "https://example.com/mindfulness.jpg",
                    "A beginner's guide to mindfulness meditation. Learn techniques to reduce stress, improve focus, and enhance your overall well-being with simple daily practices.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 8, // 8 hours ago
                    276,
                    "Health & Wellness",
                    "Mental Health"
            ));
            
            allArticles.add(createSampleArticle(
                    "Renewable Energy Breakthroughs",
                    "Energy Reporter",
                    "https://example.com/renewable-energy.jpg",
                    "The latest advancements in renewable energy technology that are making clean power more efficient and affordable. From solar panels to wind turbines, the future looks bright.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 12, // 12 hours ago
                    198,
                    "Science & Environment",
                    "Renewable Energy"
            ));
            
            // Page 2
            allArticles.add(createSampleArticle(
                    "Blockchain Beyond Cryptocurrency",
                    "Tech Analyst",
                    "https://example.com/blockchain.jpg",
                    "Exploring real-world applications of blockchain technology in supply chain, healthcare, and voting systems. Discover how blockchain is transforming industries beyond finance.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 15, // 15 hours ago
                    231,
                    "Technology",
                    "Blockchain"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Art of Sourdough",
                    "Master Baker",
                    "https://example.com/sourdough.jpg",
                    "A comprehensive guide to making the perfect sourdough bread at home. Learn about starters, fermentation, and baking techniques for that perfect crust and crumb.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 18, // 18 hours ago
                    324,
                    "Food & Cooking",
                    "Baking"
            ));
            
            allArticles.add(createSampleArticle(
                    "Space Tourism: The Next Frontier",
                    "Space Enthusiast",
                    "https://example.com/space-tourism.jpg",
                    "How commercial space travel is becoming a reality. Explore the companies leading the charge and what it takes to become a space tourist.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 22, // 22 hours ago
                    287,
                    "Science & Technology",
                    "Space Exploration"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Electric Vehicles",
                    "Auto Expert",
                    "https://example.com/electric-vehicles.jpg",
                    "What to expect from the next generation of electric cars. From longer ranges to faster charging, the EV revolution is just getting started.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 26, // 26 hours ago
                    276,
                    "Automotive",
                    "Electric Vehicles"
            ));
            
            allArticles.add(createSampleArticle(
                    "Minimalist Living: Less is More",
                    "Lifestyle Coach",
                    "https://example.com/minimalism.jpg",
                    "How embracing minimalism can lead to a more fulfilling life. Tips for decluttering your space and mind to focus on what truly matters.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 30, // 30 hours ago
                    198,
                    "Lifestyle",
                    "Minimalism"
            ));
            
            // Page 3
            allArticles.add(createSampleArticle(
                    "The Science of Sleep",
                    "Sleep Specialist",
                    "https://example.com/sleep-science.jpg",
                    "Understanding the importance of quality sleep and how it affects your health. Learn about sleep cycles, common disorders, and tips for better rest.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 36, // 36 hours ago
                    312,
                    "Health & Wellness",
                    "Sleep"
            ));
            
            allArticles.add(createSampleArticle(
                    "Sustainable Fashion Trends",
                    "Fashion Editor",
                    "https://example.com/sustainable-fashion.jpg",
                    "How the fashion industry is embracing sustainability. Discover eco-friendly brands and learn how to build a more sustainable wardrobe.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 42, // 42 hours ago
                    245,
                    "Fashion",
                    "Sustainable Fashion"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Work: AI and Automation",
                    "Future of Work Expert",
                    "https://example.com/future-work.jpg",
                    "How artificial intelligence and automation are reshaping the job market. Which careers will thrive and which might disappear in the coming years?",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
                    298,
                    "Business",
                    "Future of Work"
            ));
            
            allArticles.add(createSampleArticle(
                    "Plant-Based Nutrition",
                    "Nutritionist",
                    "https://example.com/plant-based.jpg",
                    "The health benefits of a plant-based diet and how to ensure you're getting all the necessary nutrients. Delicious recipes included!",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 52, // 52 hours ago
                    231,
                    "Health & Wellness",
                    "Nutrition"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Rise of Smart Cities",
                    "Urban Planner",
                    "https://example.com/smart-cities.jpg",
                    "How technology is making cities more efficient, sustainable, and livable. From smart traffic lights to waste management, the future of urban living.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 60, // 2.5 days ago
                    187,
                    "Technology",
                    "Smart Cities"
            ));
            
            // Page 4
            allArticles.add(createSampleArticle(
                    "The Psychology of Habits",
                    "Behavioral Psychologist",
                    "https://example.com/habits.jpg",
                    "Understanding how habits are formed and how to change them. Science-backed strategies for building good habits and breaking bad ones.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 66, // 2.75 days ago
                    276,
                    "Psychology",
                    "Habit Formation"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Social Media",
                    "Digital Marketer",
                    "https://example.com/social-media-future.jpg",
                    "Emerging trends in social media and how they're changing the way we connect. From the metaverse to ephemeral content, what's next?",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 72, // 3 days ago
                    312,
                    "Technology",
                    "Social Media"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Science of Happiness",
                    "Positive Psychologist",
                    "https://example.com/happiness.jpg",
                    "Research-backed strategies for increasing happiness and well-being. Learn about the psychology of happiness and how to apply it to your daily life.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 80, // ~3.3 days ago
                    289,
                    "Psychology",
                    "Positive Psychology"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Future of Renewable Energy Storage",
                    "Energy Analyst",
                    "https://example.com/energy-storage.jpg",
                    "Breakthroughs in battery technology that are making renewable energy more reliable. How energy storage is the key to a sustainable energy future.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 90, // ~3.75 days ago
                    201,
                    "Science & Environment",
                    "Energy Storage"
            ));
            
            allArticles.add(createSampleArticle(
                    "The Art of Digital Detox",
                    "Wellness Coach",
                    "https://example.com/digital-detox.jpg",
                    "How to unplug and recharge in our always-connected world. Practical tips for reducing screen time and improving your digital well-being.",
                    System.currentTimeMillis() - 1000 * 60 * 60 * 96, // 4 days ago
                    267,
                    "Lifestyle",
                    "Digital Wellness"
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
        // Handle article click (navigate to detail)
        if (getActivity() != null) {
            ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(article);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, articleDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    
    // Helper method to create sample articles
    private Article createSampleArticle(String title, String authorName, String imageUrl, 
                                      String content, long timestamp, int viewCount,
                                      String categoryId, String subcategoryId) {
        Article article = new Article();
        article.setId(java.util.UUID.randomUUID().toString());
        article.setTitle(title);
        article.setContent(content);
        article.setImageUrl(imageUrl);
        article.setCategory(categoryId);
        article.setCategoryId(categoryId);
        article.setSubcategoryId(subcategoryId);
        article.setAuthorName(authorName);
        article.setPublishDate(new java.util.Date(timestamp));
        article.setViewCount(viewCount);
        return article;
    }
}
