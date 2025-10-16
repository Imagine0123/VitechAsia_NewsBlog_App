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
import com.rafdi.vitechasia.blog.models.Category;
import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying articles within a specific category.
 * Shows subcategories and their associated articles.
 */
public class CategoryFragment extends Fragment implements 
        ArticleVerticalAdapter.OnArticleClickListener,
        SubcategoryAdapter.OnViewAllClickListener,
        SubcategoryAdapter.OnArticleClickListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private String categoryId;
    private Category category;
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
        // Find the category object from DummyDataGenerator
        category = findCategoryById(categoryId);
        
        if (category == null) {
            return;
        }
        
        // Get subcategories from the Category object
        List<String> subcategories = category.getSubcategories();
        
        // Filter subcategories that have articles
        List<String> subcategoriesWithArticles = new ArrayList<>();
        
        for (String subcategoryId : subcategories) {
            List<Article> articles = category.getArticlesForSubcategory(subcategoryId);
            if (!articles.isEmpty()) {
                subcategoriesWithArticles.add(subcategoryId);
            }
        }
        
        // If no subcategories with articles found, use all available subcategories
        if (subcategoriesWithArticles.isEmpty()) {
            subcategoriesWithArticles.addAll(subcategories);
        }
        
        subcategoryAdapter.setSubcategories(subcategoriesWithArticles);
    }
    
    private Category findCategoryById(String categoryId) {
        List<Category> categories = DataHandler.getAllCategories();
        for (Category cat : categories) {
            if (cat.getId().equals(categoryId)) {
                return cat;
            }
        }
        return null;
    }
    
    private String[] getSubcategoryIdsForCategory(String categoryId) {
        switch (categoryId) {
            case DataHandler.CATEGORY_TECH:
                return new String[]{
                    DataHandler.SUBCATEGORY_ANDROID,
                    DataHandler.SUBCATEGORY_IOS,
                    DataHandler.SUBCATEGORY_WEB,
                    DataHandler.SUBCATEGORY_AI
                };
            case DataHandler.CATEGORY_HEALTH:
                return new String[]{
                    DataHandler.SUBCATEGORY_FITNESS,
                    DataHandler.SUBCATEGORY_NUTRITION,
                    DataHandler.SUBCATEGORY_MENTAL_HEALTH
                };
            case DataHandler.CATEGORY_SPORTS:
                return new String[]{
                    DataHandler.SUBCATEGORY_FOOTBALL,
                    DataHandler.SUBCATEGORY_BASKETBALL,
                    DataHandler.SUBCATEGORY_TENNIS
                };
            case DataHandler.CATEGORY_NEWS:
                return new String[]{
                    DataHandler.SUBCATEGORY_WORLD,
                    DataHandler.SUBCATEGORY_POLITICS,
                    DataHandler.SUBCATEGORY_ECONOMY
                };
            default:
                return new String[0];
        }
    }
    
    private String[] getSubcategoryNamesForCategory(String categoryId) {
        switch (categoryId) {
            case DataHandler.CATEGORY_TECH:
                return getResources().getStringArray(R.array.subcategories_tech);
            case DataHandler.CATEGORY_HEALTH:
                return getResources().getStringArray(R.array.subcategories_health);
            case DataHandler.CATEGORY_SPORTS:
                return getResources().getStringArray(R.array.subcategories_sports);
            case DataHandler.CATEGORY_NEWS:
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
            case DataHandler.CATEGORY_TECH:
                return getString(R.string.category_tech);
            case DataHandler.CATEGORY_HEALTH:
                return getString(R.string.category_health);
            case DataHandler.CATEGORY_SPORTS:
                return getString(R.string.category_sports);
            case DataHandler.CATEGORY_NEWS:
                return getString(R.string.category_news);
            default:
                return categoryId;
        }
    }
}
