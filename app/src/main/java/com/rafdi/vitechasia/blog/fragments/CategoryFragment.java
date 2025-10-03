package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.adapters.SubcategoryAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment implements 
        ArticleVerticalAdapter.OnArticleClickListener,
        SubcategoryAdapter.OnViewAllClickListener,
        SubcategoryAdapter.OnArticleClickListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private String categoryId;
    private SubcategoryAdapter subcategoryAdapter;

    public static CategoryFragment newInstance(String categoryId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Initialize views
        RecyclerView subcategoriesRecyclerView = view.findViewById(R.id.subcategoriesRecyclerView);
        TextView categoryTitle = view.findViewById(R.id.categoryTitle);

        // Set category title
        categoryTitle.setText(getCategoryDisplayName(categoryId));

        // Setup RecyclerView with SubcategoryAdapter
        subcategoryAdapter = new SubcategoryAdapter(categoryId, this, this);
        subcategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        subcategoriesRecyclerView.setAdapter(subcategoryAdapter);

        // Load subcategories for this category
        loadSubcategories();

        return view;
    }

    private void loadSubcategories() {
        // Get articles for this category
        List<Article> categoryArticles = DummyDataGenerator.getDummyArticlesByCategory(categoryId);
        
        // Create a map to store subcategory name to display name mapping
        Map<String, String> subcategoryMap = new HashMap<>();
        
        // Get all possible subcategories for this category
        String[] subcategoryIds = getSubcategoryIdsForCategory(categoryId);
        String[] subcategoryNames = getSubcategoryNamesForCategory(categoryId);
        
        // Create mapping of subcategory IDs to display names
        for (int i = 0; i < subcategoryIds.length; i++) {
            subcategoryMap.put(subcategoryIds[i], subcategoryNames[i]);
        }
        
        // Create a list to store subcategories that have articles
        List<String> subcategoriesWithArticles = new ArrayList<>();
        
        // Check which subcategories have articles
        for (String subcategoryId : subcategoryMap.keySet()) {
            List<Article> subcategoryArticles = DummyDataGenerator.getDummyArticlesBySubcategory(subcategoryId);
            if (!subcategoryArticles.isEmpty()) {
                subcategoriesWithArticles.add(subcategoryId);
            }
        }
        
        // If no subcategories with articles found, use all available subcategories
        if (subcategoriesWithArticles.isEmpty()) {
            subcategoriesWithArticles.addAll(subcategoryMap.keySet());
        }
        
        subcategoryAdapter.setSubcategories(subcategoriesWithArticles);
    }
    
    private String[] getSubcategoryIdsForCategory(String categoryId) {
        switch (categoryId) {
            case DummyDataGenerator.CATEGORY_TECH:
                return new String[]{
                    DummyDataGenerator.SUBCATEGORY_ANDROID,
                    DummyDataGenerator.SUBCATEGORY_IOS,
                    DummyDataGenerator.SUBCATEGORY_WEB,
                    DummyDataGenerator.SUBCATEGORY_AI
                };
            case DummyDataGenerator.CATEGORY_HEALTH:
                return new String[]{
                    DummyDataGenerator.SUBCATEGORY_FITNESS,
                    DummyDataGenerator.SUBCATEGORY_NUTRITION,
                    DummyDataGenerator.SUBCATEGORY_MENTAL_HEALTH
                };
            case DummyDataGenerator.CATEGORY_SPORTS:
                return new String[]{
                    DummyDataGenerator.SUBCATEGORY_FOOTBALL,
                    DummyDataGenerator.SUBCATEGORY_BASKETBALL,
                    DummyDataGenerator.SUBCATEGORY_TENNIS
                };
            case DummyDataGenerator.CATEGORY_NEWS:
                return new String[]{
                    DummyDataGenerator.SUBCATEGORY_WORLD,
                    DummyDataGenerator.SUBCATEGORY_POLITICS,
                    DummyDataGenerator.SUBCATEGORY_ECONOMY
                };
            default:
                return new String[0];
        }
    }
    
    private String[] getSubcategoryNamesForCategory(String categoryId) {
        switch (categoryId) {
            case DummyDataGenerator.CATEGORY_TECH:
                return getResources().getStringArray(R.array.subcategories_tech);
            case DummyDataGenerator.CATEGORY_HEALTH:
                return getResources().getStringArray(R.array.subcategories_health);
            case DummyDataGenerator.CATEGORY_SPORTS:
                return getResources().getStringArray(R.array.subcategories_sports);
            case DummyDataGenerator.CATEGORY_NEWS:
                return getResources().getStringArray(R.array.subcategories_news);
            default:
                return new String[0];
        }
    }

    @Override
    public void onViewAllClick(String categoryId, String subcategoryName) {
        navigateToSubcategory(categoryId, subcategoryName);
    }

    @Override
    public void onArticleClick(Article article) {
        // Handle article click from subcategory
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, ArticleDetailFragment.newInstance(article));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void navigateToSubcategory(String categoryId, String subcategoryName) {
        if (getActivity() != null) {
            Fragment subcategoryFragment = SubcategoryFragment.newInstance(
                    getCategoryDisplayName(categoryId), 
                    subcategoryName);
            
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, subcategoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private String getCategoryDisplayName(String categoryId) {
        if (categoryId == null) return "";
        switch (categoryId.toLowerCase()) {
            case DummyDataGenerator.CATEGORY_TECH:
                return getString(R.string.category_tech);
            case DummyDataGenerator.CATEGORY_HEALTH:
                return getString(R.string.category_health);
            case DummyDataGenerator.CATEGORY_SPORTS:
                return getString(R.string.category_sports);
            case DummyDataGenerator.CATEGORY_NEWS:
                return getString(R.string.category_news);
            default:
                return categoryId;
        }
    }
}
