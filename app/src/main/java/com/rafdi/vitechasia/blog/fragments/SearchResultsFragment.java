package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.activities.HomePage;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.models.Category;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying search results based on user queries.
 * Shows filtered articles with category-based search capabilities.
 */
public class SearchResultsFragment extends Fragment implements ArticleVerticalAdapter.OnArticleClickListener {
    private static final String ARG_QUERY = "search_query";

    private String searchQuery;
    private String selectedCategory = null;
    private String selectedSubcategory = null;
    private RecyclerView resultsRecyclerView;
    private ArticleVerticalAdapter verticalAdapter;
    private TextView noResultsText;
    private ChipGroup chipGroupFilters;
    private List<Article> allSearchResults = new ArrayList<>();

    public static SearchResultsFragment newInstance(String query) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery = getArguments().getString(ARG_QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        // Initialize views
        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView);
        noResultsText = view.findViewById(R.id.noResultsText);
        chipGroupFilters = view.findViewById(R.id.chip_group_filters);
        view.findViewById(R.id.btn_filter).setOnClickListener(v -> showFilterDialog());

        // Setup RecyclerView
        verticalAdapter = new ArticleVerticalAdapter(new ArrayList<>(), this);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        resultsRecyclerView.setAdapter(verticalAdapter);

        // Perform initial search
        performSearch();

        return view;
    }

    @Override
    public void onArticleClick(Article article) {
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).loadArticleDetailFragment(article);
        }
    }

    private void performSearch() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            showNoResults();
            return;
        }

        try {
            // Perform search using DummyDataGenerator
            allSearchResults = DummyDataGenerator.searchArticles(searchQuery);
            applyFilters();
        } catch (Exception e) {
            e.printStackTrace();
            showNoResults();
        }
    }

    private void applyFilters() {
        if (allSearchResults == null || allSearchResults.isEmpty()) {
            showNoResults();
            return;
        }

        List<Article> filteredResults = new ArrayList<>();
        for (Article article : allSearchResults) {
            boolean matchesCategory = selectedCategory == null ||
                (article.getCategoryId() != null && article.getCategoryId().equalsIgnoreCase(selectedCategory));

            boolean matchesSubcategory = selectedSubcategory == null ||
                (article.getSubcategoryId() != null && article.getSubcategoryId().equalsIgnoreCase(selectedSubcategory));

            if (matchesCategory && matchesSubcategory) {
                filteredResults.add(article);
            }
        }

        // Update UI
        requireActivity().runOnUiThread(() -> {
            if (filteredResults.isEmpty()) {
                showNoResults();
            } else {
                showResults(filteredResults);
            }
            updateFilterChips();
        });
    }

    private void showFilterDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        ChipGroup categoryChipGroup = dialogView.findViewById(R.id.category_chip_group);
        ChipGroup subcategoryChipGroup = dialogView.findViewById(R.id.subcategory_chip_group);
        View subcategoriesLabel = dialogView.findViewById(R.id.subcategories_label);
        com.google.android.material.button.MaterialButton btnApply = dialogView.findViewById(R.id.btn_apply);
        com.google.android.material.button.MaterialButton btnReset = dialogView.findViewById(R.id.btn_reset);

        // Get categories with their IDs
        List<Category> categories = DummyDataGenerator.getAllCategories();

        // Add category chips
        for (Category category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category.getName());
            chip.setCheckable(true);
            chip.setChecked(category.getId().equals(selectedCategory));
            
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Clear previous selection
                    for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
                        View child = categoryChipGroup.getChildAt(i);
                        if (child instanceof Chip && child != buttonView) {
                            ((Chip) child).setChecked(false);
                        }
                    }
                    selectedCategory = category.getId();
                    selectedSubcategory = null;
                    updateSubcategoryChips(subcategoryChipGroup, subcategoriesLabel, selectedCategory);
                }
            });
            categoryChipGroup.addView(chip);
        }

        // Initialize subcategories if a category is already selected
        if (selectedCategory != null) {
            updateSubcategoryChips(subcategoryChipGroup, subcategoriesLabel, selectedCategory);
        }

        // Set up dialog with default theme
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        // Apply window background color for dark mode
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Set up button click listeners
        btnApply.setOnClickListener(v -> {
            applyFilters();
            dialog.dismiss();
        });

        btnReset.setOnClickListener(v -> {
            selectedCategory = null;
            selectedSubcategory = null;
            categoryChipGroup.clearCheck();
            subcategoryChipGroup.clearCheck();
            subcategoryChipGroup.setVisibility(View.GONE);
            subcategoriesLabel.setVisibility(View.GONE);
            applyFilters();
        });

        dialog.show();
    }

    private void updateSubcategoryChips(ChipGroup subcategoryChipGroup, View subcategoriesLabel, String category) {
        if (subcategoryChipGroup == null || subcategoriesLabel == null) return;

        subcategoryChipGroup.removeAllViews();
        List<String> subcategories = DummyDataGenerator.getSubcategoriesForCategory(category);

        if (subcategories != null && !subcategories.isEmpty()) {
            subcategoriesLabel.setVisibility(View.VISIBLE);
            subcategoryChipGroup.setVisibility(View.VISIBLE);

            for (String subcategory : subcategories) {
                if (subcategory == null) continue;

                // Capitalize the first letter of the subcategory
                String displayName = subcategory.substring(0, 1).toUpperCase() + 
                                  (subcategory.length() > 1 ? subcategory.substring(1) : "");

                Chip chip = new Chip(requireContext());
                chip.setText(displayName);
                chip.setCheckable(true);
                chip.setChecked(displayName.equals(selectedSubcategory));
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedSubcategory = displayName;
                    }
                });
                subcategoryChipGroup.addView(chip);
            }
        } else {
            subcategoriesLabel.setVisibility(View.GONE);
            subcategoryChipGroup.setVisibility(View.GONE);
        }
    }

    private void updateFilterChips() {
        if (chipGroupFilters == null) return;

        chipGroupFilters.removeAllViews();

        if (selectedCategory != null) {
            String displayName = getDisplayNameForCategory(selectedCategory);
            addFilterChip(displayName, v -> {
                selectedCategory = null;
                selectedSubcategory = null;
                applyFilters();
            });
        }

        if (selectedSubcategory != null) {
            addFilterChip(selectedSubcategory, v -> {
                selectedSubcategory = null;
                applyFilters();
            });
        }
    }

    private String getDisplayNameForCategory(String categoryId) {
        if (categoryId == null) return "";
        for (Category category : DummyDataGenerator.getAllCategories()) {
            if (categoryId.equalsIgnoreCase(category.getId())) {
                return category.getName();
            }
        }
        return categoryId;
    }

    private void addFilterChip(String text, View.OnClickListener onCloseClick) {
        if (chipGroupFilters == null || text == null) return;

        // Create chip with Material 3 style
        Chip chip = new Chip(requireContext());
        
        // Set chip text and appearance
        chip.setText(text);
        chip.setCloseIconVisible(true);
        
        // Use theme attributes for colors
        chip.setChipBackgroundColorResource(R.color.primary);
        chip.setTextColor(getResources().getColor(R.color.on_primary, requireContext().getTheme()));
        chip.setCloseIconTintResource(R.color.on_primary);

        // Set click listener for close icon
        chip.setOnCloseIconClickListener(v -> {
            if (onCloseClick != null) {
                onCloseClick.onClick(v);
            }
            chipGroupFilters.removeView(chip);
        });

        chipGroupFilters.addView(chip);
    }

    private void showResults(List<Article> results) {
        if (resultsRecyclerView != null && noResultsText != null) {
            resultsRecyclerView.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);

            if (verticalAdapter != null) {
                verticalAdapter.setArticles(results);
            }
        }
    }

    private void showNoResults() {
        noResultsText.setVisibility(View.VISIBLE);
        resultsRecyclerView.setVisibility(View.GONE);
        noResultsText.setText(getString(R.string.no_results_found, searchQuery));
    }
}
