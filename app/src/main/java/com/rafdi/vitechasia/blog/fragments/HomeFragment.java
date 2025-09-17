package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleHorizontalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.models.Category;

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

    private void showCategoryArticles(String categoryId) {
        // Navigate to the CategoryFragment with the selected category ID
        if (getActivity() != null) {
            CategoryFragment fragment = CategoryFragment.newInstance(categoryId);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
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
    
    // Add this method to handle subcategory clicks
    public void onSubcategoryClick(String categoryName, String subcategoryName) {
        if (getActivity() != null) {
            SubcategoryFragment fragment = SubcategoryFragment.newInstance(categoryName, subcategoryName);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // Sample data loading (replace with your actual data loading logic)
    private void loadSampleData() {
        // Clear existing data
        allArticles.clear();
        
        // Sample categories with subcategories
        List<Category> categories = new ArrayList<>();
        
        // Sports Category
        Category sports = new Category("sports", "Sports", new ArrayList<>());
        sports.addSubcategory("Football");
        sports.addSubcategory("Basketball");
        sports.addSubcategory("Tennis");
        categories.add(sports);
        
        // Add sample sports articles
        allArticles.add(createSampleArticle(
            "Football: Team Wins Championship",
            "Sports Reporter",
            "https://picsum.photos/400/300?random=1",
            "The home team has won the championship in a thrilling match...",
            System.currentTimeMillis() - 1000 * 60 * 60 * 5, // 5 hours ago
            250,
            "sports",
            "Football"
        ));
        
        // Technology Category
        Category tech = new Category("tech", "Technology", new ArrayList<>());
        tech.addSubcategory("Programming");
        tech.addSubcategory("Gadgets");
        tech.addSubcategory("AI");
        categories.add(tech);
        
        // Add sample tech articles
        allArticles.add(createSampleArticle(
            "New Programming Language Released",
            "Tech Expert",
            "https://picsum.photos/400/300?random=2",
            "A new programming language has been released with innovative features...",
            System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
            180,
            "tech",
            "Programming"
        ));
        
        allArticles.add(createSampleArticle(
            "Latest Smartphone Review",
            "Gadget Guru",
            "https://picsum.photos/400/300?random=3",
            "The latest smartphone offers amazing features and performance...",
            System.currentTimeMillis() - 1000 * 60 * 60 * 8, // 8 hours ago
            320,
            "tech",
            "Gadgets"
        ));
        
        allArticles.add(createSampleArticle(
            "AI Breakthrough in Healthcare",
            "AI Researcher",
            "https://picsum.photos/400/300?random=4",
            "New AI technology is revolutionizing healthcare diagnostics...",
            System.currentTimeMillis() - 1000 * 60 * 60 * 12, // 12 hours ago
            450,
            "tech",
            "AI"
        ));
        
        // News Category
        Category news = new Category("news", "News", new ArrayList<>());
        news.addSubcategory("World");
        news.addSubcategory("Politics");
        news.addSubcategory("Business");
        categories.add(news);
        
        // Add sample news articles
        allArticles.add(createSampleArticle(
            "Global Summit Begins",
            "News Desk",
            "https://picsum.photos/400/300?random=5",
            "World leaders gather for the annual global summit...",
            System.currentTimeMillis() - 1000 * 60 * 60 * 3, // 3 hours ago
            190,
            "news",
            "World"
        ));
        
        // Update adapters with the new data
        updateAdapters();
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
