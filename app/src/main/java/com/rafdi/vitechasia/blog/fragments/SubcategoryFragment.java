package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.models.Category;
import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying articles within a specific subcategory.
 * Shows detailed articles for the selected subcategory.
 */
public class SubcategoryFragment extends Fragment implements ArticleVerticalAdapter.OnArticleClickListener {
    private static final String ARG_CATEGORY_NAME = "category_name";
    private static final String ARG_SUBCATEGORY_NAME = "subcategory_name";

    private String categoryName;
    private String subcategoryName;
    private RecyclerView articlesRecyclerView;
    private ArticleVerticalAdapter verticalAdapter;

    public static SubcategoryFragment newInstance(String categoryName, String subcategoryName) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        args.putString(ARG_SUBCATEGORY_NAME, subcategoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            subcategoryName = getArguments().getString(ARG_SUBCATEGORY_NAME);
        }

        // Initialize DataHandler to enable API-first functionality
        if (getContext() != null) {
            DataHandler.initialize(getContext().getApplicationContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);

        // Initialize views
        articlesRecyclerView = view.findViewById(R.id.articlesRecyclerView);
        TextView subcategoryTitle = view.findViewById(R.id.subcategoryTitle);
        TextView noArticlesText = view.findViewById(R.id.noArticlesText);

        // Set title with proper capitalization
        String formattedCategory = categoryName != null && !categoryName.isEmpty() ?
                categoryName.substring(0, 1).toUpperCase() + 
                (categoryName.length() > 1 ? categoryName.substring(1).toLowerCase() : "") : "";
                
        String formattedSubcategory = getSubcategoryDisplayName(subcategoryName);
        formattedSubcategory = formattedSubcategory != null && !formattedSubcategory.isEmpty() ?
                formattedSubcategory.substring(0, 1).toUpperCase() + 
                (formattedSubcategory.length() > 1 ? formattedSubcategory.substring(1).toLowerCase() : "") : "";
                
        String title = String.format("%s - %s", formattedCategory, formattedSubcategory);
        subcategoryTitle.setText(title);

        // Setup RecyclerView
        verticalAdapter = new ArticleVerticalAdapter(new ArrayList<>(), this);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        articlesRecyclerView.setAdapter(verticalAdapter);

        // Load articles
        loadArticles(noArticlesText);

        return view;
    }

    private void loadArticles(TextView noArticlesText) {
        // Show loading state
        noArticlesText.setVisibility(View.GONE);
        articlesRecyclerView.setVisibility(View.GONE);
        
        // Find the category that contains this subcategory
        Category category = findCategoryBySubcategory(subcategoryName);
        
        if (category == null) {
            noArticlesText.setVisibility(View.VISIBLE);
            noArticlesText.setText(getString(R.string.error_loading));
            return;
        }
        
        // Show loading indicator or message if needed
        noArticlesText.setText(getString(R.string.loading));
        noArticlesText.setVisibility(View.VISIBLE);
        
        // Use the async method to get articles
        category.getArticlesForSubcategory(subcategoryName, new DataHandler.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Article> subcategoryArticles) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    if (subcategoryArticles == null || subcategoryArticles.isEmpty()) {
                        noArticlesText.setVisibility(View.VISIBLE);
                        articlesRecyclerView.setVisibility(View.GONE);
                        noArticlesText.setText(getString(R.string.no_articles_found));
                    } else {
                        noArticlesText.setVisibility(View.GONE);
                        articlesRecyclerView.setVisibility(View.VISIBLE);
                        verticalAdapter.setArticles(subcategoryArticles);
                    }
                });
            }

            @Override
            public void onError(String message) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    noArticlesText.setVisibility(View.VISIBLE);
                    articlesRecyclerView.setVisibility(View.GONE);
                    noArticlesText.setText(getString(R.string.error_loading));
                    Log.e("SubcategoryFragment", "Error loading articles: " + message);
                });
            }
        });
    }
    
    private Category findCategoryBySubcategory(String subcategoryId) {
        List<Category> categories = DataHandler.getAllCategories();
        for (Category category : categories) {
            if (category.getSubcategories().contains(subcategoryId)) {
                return category;
            }
        }
        return null;
    }

    private String getSubcategoryDisplayName(String subcategoryId) {
        if (subcategoryId == null) return "";

        // Map subcategory IDs to display names
        switch (subcategoryId.toLowerCase()) {
            case DataHandler.SUBCATEGORY_ANDROID:
                return getString(R.string.subcategory_android);
            case DataHandler.SUBCATEGORY_IOS:
                return getString(R.string.subcategory_ios);
            case DataHandler.SUBCATEGORY_WEB:
                return getString(R.string.subcategory_web);
            case DataHandler.SUBCATEGORY_AI:
                return getString(R.string.subcategory_ai);
            case DataHandler.SUBCATEGORY_FITNESS:
                return getString(R.string.subcategory_fitness);
            case DataHandler.SUBCATEGORY_NUTRITION:
                return getString(R.string.subcategory_nutrition);
            case DataHandler.SUBCATEGORY_MENTAL_HEALTH:
                return getString(R.string.subcategory_mental_health);
            case DataHandler.SUBCATEGORY_FOOTBALL:
                return getString(R.string.subcategory_football);
            case DataHandler.SUBCATEGORY_BASKETBALL:
                return getString(R.string.subcategory_basketball);
            case DataHandler.SUBCATEGORY_TENNIS:
                return getString(R.string.subcategory_tennis);
            case DataHandler.SUBCATEGORY_WORLD:
                return getString(R.string.subcategory_world);
            case DataHandler.SUBCATEGORY_POLITICS:
                return getString(R.string.subcategory_politics);
            case DataHandler.SUBCATEGORY_ECONOMY:
                return getString(R.string.subcategory_economy);
            default:
                // Capitalize first letter as fallback
                return subcategoryId.substring(0, 1).toUpperCase() +
                       (subcategoryId.length() > 1 ? subcategoryId.substring(1).toLowerCase() : "");
        }
    }

    @Override
    public void onArticleClick(Article article) {
        // Handle article click - navigate to ArticleDetailFragment
        if (getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, ArticleDetailFragment.newInstance(article));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
