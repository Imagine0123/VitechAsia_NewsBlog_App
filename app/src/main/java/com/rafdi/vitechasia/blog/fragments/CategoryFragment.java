package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.SubcategoryAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.models.Category;
import com.rafdi.vitechasia.blog.utils.CategoryManager;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements 
        SubcategoryAdapter.OnViewAllClickListener,
        SubcategoryAdapter.OnArticleClickListener {

    private static final String ARG_CATEGORY_ID = "category_id";

    private String categoryId;
    private String categoryName;
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
            categoryName = getCategoryName(categoryId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Initialize views
        TextView categoryTitle = view.findViewById(R.id.categoryTitle);
        categoryTitle.setText(categoryName);

        // Set up RecyclerView for subcategories
        RecyclerView subcategoriesRecyclerView = view.findViewById(R.id.subcategoriesRecyclerView);
        subcategoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        // Initialize adapter
        subcategoryAdapter = new SubcategoryAdapter(categoryId, this, this);
        subcategoriesRecyclerView.setAdapter(subcategoryAdapter);

        // Load subcategories
        loadSubcategories();

        return view;
    }

    private String getCategoryName(String categoryId) {
        if (categoryId == null) return getString(R.string.category);
        
        switch (categoryId) {
            case "sports":
                return getString(R.string.category_sports);
            case "tech":
                return getString(R.string.category_tech);
            case "news":
                return getString(R.string.category_news);
            default:
                return getString(R.string.category);
        }
    }

    private void loadSubcategories() {
        // Get the category
        Category category = CategoryManager.getInstance().getCategory(categoryId);
        if (category != null && category.hasSubcategories()) {
            subcategoryAdapter.setSubcategories(category.getSubcategories());
        } else {
            subcategoryAdapter.setSubcategories(new ArrayList<>());
        }
    }

    @Override
    public void onViewAllClick(String categoryId, String subcategoryName) {
        // Navigate to SubcategoryFragment when "View All" is clicked
        if (getActivity() != null) {
            SubcategoryFragment fragment = SubcategoryFragment.newInstance(categoryId, subcategoryName);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onArticleClick(Article article) {
        // Handle article click within the subcategory
        if (getActivity() != null) {
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
