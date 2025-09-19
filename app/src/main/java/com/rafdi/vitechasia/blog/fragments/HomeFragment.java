package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleHorizontalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setupHorizontalRecyclerView(latestArticlesRecyclerView, "latest");
        
        // Popular Articles
        setupHorizontalRecyclerView(popularArticlesRecyclerView, "popular");
        
        // Category Articles
        setupHorizontalRecyclerView(sportsArticlesRecyclerView, DummyDataGenerator.CATEGORY_SPORTS);
        setupHorizontalRecyclerView(techArticlesRecyclerView, DummyDataGenerator.CATEGORY_TECH);
        setupHorizontalRecyclerView(newsArticlesRecyclerView, DummyDataGenerator.CATEGORY_NEWS);
    }
    
    private void setupHorizontalRecyclerView(RecyclerView recyclerView, String type) {
        if (recyclerView == null) return;
        
        recyclerView.setLayoutManager(new LinearLayoutManager(
            requireContext(), 
            LinearLayoutManager.HORIZONTAL, 
            false
        ));
        
        ArticleHorizontalAdapter adapter = new ArticleHorizontalAdapter(this);
        recyclerView.setAdapter(adapter);
        
        // Load appropriate data based on type
        if ("latest".equals(type)) {
            List<Article> latestArticles = DummyDataGenerator.getDummyArticles();
            latestArticles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()));
            adapter.setArticles(latestArticles.subList(0, Math.min(5, latestArticles.size())));
        } else if ("popular".equals(type)) {
            List<Article> popularArticles = new ArrayList<>(DummyDataGenerator.getDummyArticles());
            popularArticles.sort((a1, a2) -> Integer.compare(a2.getViewCount(), a1.getViewCount()));
            adapter.setArticles(popularArticles.subList(0, Math.min(5, popularArticles.size())));
        } else {
            // This is a category view
            List<Article> categoryArticles = DummyDataGenerator.getDummyArticlesByCategory(type);
            adapter.setArticles(categoryArticles.subList(0, Math.min(5, categoryArticles.size())));
        }
    }

    private void setupClickListeners() {
        // Set up View All button click listeners
        viewAllSportsButton.setOnClickListener(v -> navigateToCategory(DummyDataGenerator.CATEGORY_SPORTS));
        viewAllTechButton.setOnClickListener(v -> navigateToCategory(DummyDataGenerator.CATEGORY_TECH));
        viewAllNewsButton.setOnClickListener(v -> navigateToCategory(DummyDataGenerator.CATEGORY_NEWS));

        // Set up category card click listeners
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.sportsCard).setOnClickListener(v -> 
                navigateToCategory(DummyDataGenerator.CATEGORY_SPORTS));
            
            view.findViewById(R.id.techCard).setOnClickListener(v -> 
                navigateToCategory(DummyDataGenerator.CATEGORY_TECH));
            
            view.findViewById(R.id.newsCard).setOnClickListener(v -> 
                navigateToCategory(DummyDataGenerator.CATEGORY_NEWS));
        }
    }

    private void navigateToCategory(String categoryId) {
        if (getActivity() != null) {
            Fragment categoryFragment = CategoryFragment.newInstance(categoryId);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, categoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
}
