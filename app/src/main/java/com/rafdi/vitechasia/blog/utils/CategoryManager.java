package com.rafdi.vitechasia.blog.utils;

import com.rafdi.vitechasia.blog.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton utility class for managing blog categories and their subcategories.
 * Provides centralized access to category data throughout the application.
 */
public class CategoryManager {
    private static CategoryManager instance;
    private final Map<String, Category> categories;

    // Private constructor to prevent instantiation
    private CategoryManager() {
        categories = new HashMap<>();
        initializeCategories();
    }

    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    private void initializeCategories() {
        // Sports Category
        List<String> sportsSubcategories = new ArrayList<>();
        sportsSubcategories.add("Football");
        sportsSubcategories.add("Basketball");
        sportsSubcategories.add("Tennis");
        categories.put("sports", new Category("sports", "Sports", sportsSubcategories));

        // Tech Category
        List<String> techSubcategories = new ArrayList<>();
        techSubcategories.add("Smartphones");
        techSubcategories.add("Laptops");
        techSubcategories.add("Gadgets");
        categories.put("tech", new Category("tech", "Technology", techSubcategories));

        // News Category
        List<String> newsSubcategories = new ArrayList<>();
        newsSubcategories.add("World");
        newsSubcategories.add("Politics");
        newsSubcategories.add("Business");
        categories.put("news", new Category("news", "News", newsSubcategories));
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    public List<String> getSubcategories(String categoryId) {
        Category category = categories.get(categoryId);
        return category != null ? category.getSubcategories() : new ArrayList<>();
    }

    public boolean isValidCategory(String categoryId) {
        return categories.containsKey(categoryId);
    }

    public boolean isValidSubcategory(String categoryId, String subcategory) {
        Category category = categories.get(categoryId);
        return category != null && category.getSubcategories().contains(subcategory);
    }

    // Helper method to get category display name
    public static String getCategoryDisplayName(String categoryId) {
        switch (categoryId) {
            case "sports":
                return "Sports";
            case "tech":
                return "Technology";
            case "news":
                return "News";
            default:
                return categoryId; // Return the ID if no mapping found
        }
    }
}
