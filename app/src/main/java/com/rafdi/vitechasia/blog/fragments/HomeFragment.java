package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleHorizontalAdapter;
import com.rafdi.vitechasia.blog.fragments.ArticleDetailFragment;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.CategoryManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment implements ArticleHorizontalAdapter.OnArticleClickListener {
    
    // RecyclerViews
    private RecyclerView latestArticlesRecyclerView;
    private RecyclerView popularArticlesRecyclerView;
    private RecyclerView sportsArticlesRecyclerView;
    private RecyclerView techArticlesRecyclerView;
    private RecyclerView newsArticlesRecyclerView;
    
    // Adapters
    private ArticleHorizontalAdapter latestArticlesAdapter;
    private ArticleHorizontalAdapter popularArticlesAdapter;
    private ArticleHorizontalAdapter sportsArticlesAdapter;
    private ArticleHorizontalAdapter techArticlesAdapter;
    private ArticleHorizontalAdapter newsArticlesAdapter;
    
    // View All Buttons
    private MaterialButton viewAllSportsButton;
    private MaterialButton viewAllTechButton;
    private MaterialButton viewAllNewsButton;
    
    // Sample data (replace with your actual data source)
    private List<Article> allArticles = new ArrayList<>();
    
    public HomeFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize adapters
        latestArticlesAdapter = new ArticleHorizontalAdapter(this);
        popularArticlesAdapter = new ArticleHorizontalAdapter(this);
        sportsArticlesAdapter = new ArticleHorizontalAdapter(this);
        techArticlesAdapter = new ArticleHorizontalAdapter(this);
        newsArticlesAdapter = new ArticleHorizontalAdapter(this);
        
        // Load sample data (replace with your actual data loading logic)
        loadSampleData();
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize views
        initializeViews(view);
        setupRecyclerViews();
        setupClickListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        // Initialize RecyclerViews
        latestArticlesRecyclerView = view.findViewById(R.id.latestArticlesRecyclerView);
        popularArticlesRecyclerView = view.findViewById(R.id.popularArticlesRecyclerView);
        sportsArticlesRecyclerView = view.findViewById(R.id.sportsArticlesRecyclerView);
        techArticlesRecyclerView = view.findViewById(R.id.techArticlesRecyclerView);
        newsArticlesRecyclerView = view.findViewById(R.id.newsArticlesRecyclerView);
        
        // Initialize View All buttons
        viewAllSportsButton = view.findViewById(R.id.viewAllSportsButton);
        viewAllTechButton = view.findViewById(R.id.viewAllTechButton);
        viewAllNewsButton = view.findViewById(R.id.viewAllNewsButton);
    }
    
    private void setupRecyclerViews() {
        // Latest Articles
        latestArticlesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        latestArticlesRecyclerView.setAdapter(latestArticlesAdapter);
        
        // Popular Articles
        popularArticlesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        popularArticlesRecyclerView.setAdapter(popularArticlesAdapter);
        
        // Sports Articles
        sportsArticlesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        sportsArticlesRecyclerView.setAdapter(sportsArticlesAdapter);
        
        // Tech Articles
        techArticlesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        techArticlesRecyclerView.setAdapter(techArticlesAdapter);
        
        // News Articles
        newsArticlesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        newsArticlesRecyclerView.setAdapter(newsArticlesAdapter);
        
        // Set data to adapters
        updateAdapters();
    }
    
    private void setupClickListeners() {
        // View All buttons click listeners
        viewAllSportsButton.setOnClickListener(v -> showCategoryArticles("sports"));
        viewAllTechButton.setOnClickListener(v -> showCategoryArticles("tech"));
        viewAllNewsButton.setOnClickListener(v -> showCategoryArticles("news"));
    }
    
    private void showCategoryArticles(String category) {
        // Implement navigation to category screen or show all articles for the category
        Toast.makeText(requireContext(), "Viewing all " + category + " articles", Toast.LENGTH_SHORT).show();
        // TODO: Implement navigation to category screen
    }
    
    private void updateAdapters() {
        // Update latest articles (most recent 5 articles)
        List<Article> latestArticles = allArticles.size() > 5 ? 
                allArticles.subList(0, 5) : allArticles;
        latestArticlesAdapter.setArticles(latestArticles);
        
        // Update popular articles (sorted by views, likes, etc.)
        List<Article> popularArticles = new ArrayList<>(allArticles);
        // Sort by some popularity metric (e.g., views, likes, etc.)
        // For now, just take the first 5 as popular
        if (popularArticles.size() > 5) {
            popularArticles = popularArticles.subList(0, 5);
        }
        popularArticlesAdapter.setArticles(popularArticles);
        
        // Update category articles
        updateCategoryArticles();
    }
    
    private void updateCategoryArticles() {
        // Filter articles by category
        List<Article> sportsArticles = new ArrayList<>();
        List<Article> techArticles = new ArrayList<>();
        List<Article> newsArticles = new ArrayList<>();
        
        for (Article article : allArticles) {
            String category = article.getCategoryId();
            if (category != null) {
                switch (category) {
                    case "sports":
                        sportsArticles.add(article);
                        break;
                    case "tech":
                        techArticles.add(article);
                        break;
                    case "news":
                        newsArticles.add(article);
                        break;
                }
            }
        }
        
        // Set data to adapters (limit to 3 articles per category)
        sportsArticlesAdapter.setArticles(sportsArticles.size() > 3 ? 
                sportsArticles.subList(0, 3) : sportsArticles);
        techArticlesAdapter.setArticles(techArticles.size() > 3 ? 
                techArticles.subList(0, 3) : techArticles);
        newsArticlesAdapter.setArticles(newsArticles.size() > 3 ? 
                newsArticles.subList(0, 3) : newsArticles);
    }
    
    @Override
    public void onArticleClick(Article article) {
        // Create a new instance of ArticleDetailFragment with the clicked article
        ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(article);
        
        // Get the FragmentManager and start a transaction
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, articleDetailFragment) // Use your container view id
                .addToBackStack(null) // Add to back stack so user can navigate back
                .commit();
    }
    
    // Sample data loading (replace with your actual data loading logic)
    private void loadSampleData() {
        // This is just sample data - replace with your actual data loading logic
        allArticles.add(createSampleArticle(
                "Latest Tech News: AI Breakthrough",
                "John Doe",
                "https://example.com/tech-ai-breakthrough.jpg",
                "A major breakthrough in AI technology has been announced...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
                150,
                "tech",
                "ai"
        ));
        
        allArticles.add(createSampleArticle(
                "Football: Team Wins Championship",
                "Jane Smith",
                "https://example.com/football-championship.jpg",
                "The home team has won the championship in a thrilling match...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 5, // 5 hours ago
                250,
                "sports",
                "football"
        ));
        
        allArticles.add(createSampleArticle(
                "Breaking: Major Political Development",
                "News Desk",
                "https://example.com/political-news.jpg",
                "A significant political development has been reported...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 10, // 10 hours ago
                300,
                "news",
                "politics"
        ));
        
        // Add more sample articles...
        allArticles.add(createSampleArticle(
                "New Smartphone Launch",
                "Tech Reporter",
                "https://example.com/new-smartphone.jpg",
                "The latest smartphone model has been launched with amazing features...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
                180,
                "tech",
                "gadgets"
        ));
        
        allArticles.add(createSampleArticle(
                "Basketball Tournament Results",
                "Sports Analyst",
                "https://example.com/basketball-tournament.jpg",
                "The national basketball tournament concluded with surprising results...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 36, // 1.5 days ago
                120,
                "sports",
                "basketball"
        ));
        
        allArticles.add(createSampleArticle(
                "Global Economic Update",
                "Finance Expert",
                "https://example.com/economy-update.jpg",
                "The latest updates on the global economy and market trends...",
                System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
                90,
                "news",
                "economy"
        ));
    }
    
    // Helper method to create sample articles with proper constructor
    private Article createSampleArticle(String title, String authorName, String imageUrl, 
                                      String content, long timestamp, int viewCount,
                                      String categoryId, String subcategoryId) {
        Article article = new Article();
        article.setId(UUID.randomUUID().toString());
        article.setTitle(title);
        article.setContent(content);
        article.setImageUrl(imageUrl);
        article.setCategory(categoryId); // For backward compatibility
        article.setCategoryId(categoryId);
        article.setSubcategoryId(subcategoryId);
        article.setAuthorName(authorName);
        article.setPublishDate(new Date(timestamp));
        article.setViewCount(viewCount);
        return article;
    }
}
