package com.rafdi.vitechasia.blog.utils;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.models.Category;
import com.rafdi.vitechasia.blog.viewmodel.ArticleViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utility class for managing article data.
 * Tries to fetch data from the API first, falls back to dummy data if needed.
 */
public class DataHandler {
    private static volatile DataHandler instance;
    private ArticleViewModel articleViewModel;
    private boolean isViewModelInitialized = false;

    /**
     * Interface for receiving data load callbacks
     */
    public interface DataLoadListener {
        void onDataLoaded(List<Article> articles);

        void onError(String message);
    }

    /**
     * Interface for receiving single article callbacks
     */
    public interface SingleArticleCallback {
        void onArticleLoaded(Article article);

        void onError(String message);
    }

    private DataHandler() {
        // Private constructor to prevent direct instantiation
    }

    /**
     * Initializes the DataHandler with a Context.
     * Must be called before using any other methods.
     *
     * @param context The application context
     */
    public static void initialize(Context context) {
        if (instance == null) {
            synchronized (DataHandler.class) {
                if (instance == null) {
                    instance = new DataHandler();
                    if (context != null && context.getApplicationContext() instanceof Application) {
                        instance.articleViewModel = new ArticleViewModel((Application) context.getApplicationContext());
                        instance.isViewModelInitialized = true;
                    }
                }
            }
        }
    }

    /**
     * Gets the singleton instance of DataHandler.
     * initialize() must be called first.
     *
     * @return The DataHandler instance
     * @throws IllegalStateException if initialize() hasn't been called
     */
    public static DataHandler getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataHandler must be initialized first. Call initialize() in your Application class.");
        }
        return instance;
    }

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.";

    // Category IDs
    public static final String CATEGORY_TECH = "tech";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_LIFESTYLE = "lifestyle";
    public static final String CATEGORY_BUSINESS = "business";
    public static final String CATEGORY_SPORTS = "sports";
    public static final String CATEGORY_NEWS = "news";

    // Subcategory IDs
    public static final String SUBCATEGORY_ANDROID = "android";
    public static final String SUBCATEGORY_IOS = "ios";
    public static final String SUBCATEGORY_WEB = "web";
    public static final String SUBCATEGORY_AI = "ai";
    public static final String SUBCATEGORY_SUSTAINABILITY = "sustainability";
    public static final String SUBCATEGORY_FINANCE = "finance";
    public static final String SUBCATEGORY_FITNESS = "fitness";
    public static final String SUBCATEGORY_NUTRITION = "nutrition";
    public static final String SUBCATEGORY_MENTAL_HEALTH = "mental health";
    public static final String SUBCATEGORY_TRAVEL = "travel";
    public static final String SUBCATEGORY_FOOD = "food";
    public static final String SUBCATEGORY_FASHION = "fashion";
    public static final String SUBCATEGORY_FOOTBALL = "football";
    public static final String SUBCATEGORY_BASKETBALL = "basketball";
    public static final String SUBCATEGORY_TENNIS = "tennis";
    public static final String SUBCATEGORY_WORLD = "world";
    public static final String SUBCATEGORY_POLITICS = "politics";
    public static final String SUBCATEGORY_ECONOMY = "economy";

    private static List<Article> allArticles = null;
    private boolean isLoading = false;
    private DataLoadListener dataLoadListener;

    /**
     * Get all bookmarked articles, synced with BookmarkManager
     */
    public static List<Article> getBookmarkedArticles(android.content.Context context) {
        List<Article> allArticles = getDummyArticles();
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(context);
        bookmarkManager.syncArticleBookmarkStatus(allArticles);

        List<Article> bookmarked = new ArrayList<>();
        for (Article article : allArticles) {
            if (article.isBookmarked()) {
                bookmarked.add(article);
            }
        }
        return bookmarked;
    }

    /**
     * Get all dummy articles. The list is cached after first creation.
     */
    public static List<Article> getDummyArticles() {
        if (allArticles == null) {
            allArticles = generateDummyArticles();
        }
        return new ArrayList<>(allArticles); // Return a copy to prevent modification of cached list
    }

    /**
     * Get articles filtered by category ID, trying the API first and falling back to dummy data
     *
     * @param categoryId The category ID to filter by
     * @param callback   Callback to receive the results asynchronously
     */
    public void getArticlesByCategory(String categoryId, DataLoadListener callback) {
        if (!isViewModelInitialized) {
            // Fall back to dummy data if ViewModel isn't initialized
            List<Article> dummyArticles = getDummyArticlesByCategory(categoryId);
            if (callback != null) {
                callback.onDataLoaded(dummyArticles);
            }
            return;
        }

        // Try to get data from the API first
        articleViewModel.loadArticles(categoryId, 1, 20); // Default page 1, 20 items per page
        articleViewModel.getArticlesLiveData().observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null && !articles.isEmpty()) {
                    if (callback != null) {
                        callback.onDataLoaded(articles);
                    }
                    articleViewModel.getArticlesLiveData().removeObserver(this);
                } else {
                    // Fall back to dummy data if API returns empty
                    List<Article> dummyArticles = getDummyArticlesByCategory(categoryId);
                    if (callback != null) {
                        callback.onDataLoaded(dummyArticles);
                    }
                    articleViewModel.getArticlesLiveData().removeObserver(this);
                }
            }
        });

        // Set up error handling
        articleViewModel.getErrorLiveData().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    // On error, fall back to dummy data
                    List<Article> dummyArticles = getDummyArticlesByCategory(categoryId);
                    if (callback != null) {
                        callback.onDataLoaded(dummyArticles);
                    }
                    articleViewModel.getErrorLiveData().removeObserver(this);
                }
            }
        });
    }

    /**
     * Get articles filtered by category ID (dummy data fallback)
     */
    public static List<Article> getDummyArticlesByCategory(String categoryId) {
        List<Article> filtered = new ArrayList<>();
        for (Article article : getDummyArticles()) {
            if (categoryId.equalsIgnoreCase(article.getCategoryId())) {
                filtered.add(article);
            }
        }
        return filtered;
    }

    /**
     * Get a single article by ID, trying the API first and falling back to dummy data
     *
     * @param id       The article ID to fetch
     * @param callback Callback to receive the result asynchronously
     */
    public void getArticleById(String id, SingleArticleCallback callback) {
        if (!isViewModelInitialized) {
            // Fall back to dummy data if ViewModel isn't initialized
            Article dummyArticle = getDummyArticleById(id);
            if (callback != null) {
                callback.onArticleLoaded(dummyArticle);
            }
            return;
        }

        // Try to get data from the API first
        articleViewModel.loadArticleById(id);
        articleViewModel.getArticleLiveData().observeForever(new Observer<Article>() {
            @Override
            public void onChanged(Article article) {
                if (article != null) {
                    if (callback != null) {
                        callback.onArticleLoaded(article);
                    }
                    articleViewModel.getArticleLiveData().removeObserver(this);
                } else {
                    // Fall back to dummy data if API returns null
                    Article dummyArticle = getDummyArticleById(id);
                    if (callback != null) {
                        callback.onArticleLoaded(dummyArticle);
                    }
                    articleViewModel.getArticleLiveData().removeObserver(this);
                }
            }
        });

        // Set up error handling
        articleViewModel.getErrorLiveData().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    // On error, fall back to dummy data
                    Article dummyArticle = getDummyArticleById(id);
                    if (callback != null) {
                        callback.onArticleLoaded(dummyArticle);
                    }
                    articleViewModel.getErrorLiveData().removeObserver(this);
                }
            }
        });
    }

    /**
     * Get a single article by ID from dummy data (fallback method)
     */
    private Article getDummyArticleById(String id) {
        for (Article article : getDummyArticles()) {
            if (id.equals(article.getId())) {
                return article;
            }
        }
        return null;
    }

    /**
     * Get articles filtered by subcategory ID
     */
    public static List<Article> getDummyArticlesBySubcategory(String subcategoryId) {
        List<Article> filtered = new ArrayList<>();
        for (Article article : getDummyArticles()) {
            if (subcategoryId.equalsIgnoreCase(article.getSubcategoryId())) {
                filtered.add(article);
            }
        }
        return filtered;
    }

    /**
     * Get articles by author ID
     */
    public static List<Article> getDummyArticlesByAuthor(String authorId) {
        List<Article> filtered = new ArrayList<>();
        for (Article article : getDummyArticles()) {
            if (authorId.equals(article.getAuthorId())) {
                filtered.add(article);
            }
        }
        return filtered;
    }

    /**
     * Get the most viewed articles
     *
     * @param limit Maximum number of articles to return
     */
    public static List<Article> getMostViewedArticles(int limit) {
        List<Article> articles = new ArrayList<>(getDummyArticles());
        articles.sort((a1, a2) -> Integer.compare(a2.getViewCount(), a1.getViewCount()));
        return articles.subList(0, Math.min(limit, articles.size()));
    }

    /**
     * Get the most liked articles
     *
     * @param limit Maximum number of articles to return
     */
    public static List<Article> getMostLikedArticles(int limit) {
        List<Article> articles = new ArrayList<>(getDummyArticles());
        articles.sort((a1, a2) -> Integer.compare(a2.getLikeCount(), a1.getLikeCount()));
        return articles.subList(0, Math.min(limit, articles.size()));
    }

    /**
     * Get the newest articles
     *
     * @param limit Maximum number of articles to return
     */
    public static List<Article> getNewestArticles(int limit) {
        List<Article> articles = new ArrayList<>(getDummyArticles());
        articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()));
        return articles.subList(0, Math.min(limit, articles.size()));
    }

    /**
     * Search for articles that match the given query in title, content, or author name
     *
     * @param query The search query
     * @return List of matching articles
     */
    public static List<Article> searchArticles(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchQuery = query.toLowerCase().trim();
        List<Article> allArticles = getDummyArticles();
        List<Article> results = new ArrayList<>();

        for (Article article : allArticles) {
            if ((article.getTitle() != null && article.getTitle().toLowerCase().contains(searchQuery)) ||
                    (article.getContent() != null && article.getContent().toLowerCase().contains(searchQuery)) ||
                    (article.getAuthorName() != null && article.getAuthorName().toLowerCase().contains(searchQuery))) {
                results.add(article);
            }
        }

        return results;
    }

    /**
     * Get all available categories with their subcategories
     */
    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        // Technology
        List<String> techSubcategories = Arrays.asList(SUBCATEGORY_ANDROID, SUBCATEGORY_IOS, SUBCATEGORY_WEB, SUBCATEGORY_AI);
        categories.add(new Category(CATEGORY_TECH, "Technology", techSubcategories));

        // Health
        List<String> healthSubcategories = Arrays.asList(SUBCATEGORY_FITNESS, SUBCATEGORY_NUTRITION, SUBCATEGORY_MENTAL_HEALTH);
        categories.add(new Category(CATEGORY_HEALTH, "Health", healthSubcategories));

        // Lifestyle
        List<String> lifestyleSubcategories = Arrays.asList(SUBCATEGORY_TRAVEL, SUBCATEGORY_FOOD,
                SUBCATEGORY_FASHION);
        categories.add(new Category(CATEGORY_LIFESTYLE, "Lifestyle", lifestyleSubcategories));

        // Business
        List<String> businessSubcategories = Arrays.asList(SUBCATEGORY_FINANCE, SUBCATEGORY_ECONOMY);
        categories.add(new Category(CATEGORY_BUSINESS, "Business", businessSubcategories));

        // Sports
        List<String> sportsSubcategories = Arrays.asList(SUBCATEGORY_FOOTBALL, SUBCATEGORY_BASKETBALL, SUBCATEGORY_TENNIS);
        categories.add(new Category(CATEGORY_SPORTS, "Sports", sportsSubcategories));

        // News
        List<String> newsSubcategories = Arrays.asList(SUBCATEGORY_WORLD, SUBCATEGORY_POLITICS, SUBCATEGORY_ECONOMY);
        categories.add(new Category(CATEGORY_NEWS, "News", newsSubcategories));

        return categories;
    }

    /**
     * Get subcategories for a specific category
     */
    public static List<String> getSubcategoriesForCategory(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case CATEGORY_TECH:
                return Arrays.asList(SUBCATEGORY_ANDROID, SUBCATEGORY_IOS, SUBCATEGORY_WEB, SUBCATEGORY_AI);
            case CATEGORY_HEALTH:
                return Arrays.asList(SUBCATEGORY_FITNESS, SUBCATEGORY_NUTRITION, SUBCATEGORY_MENTAL_HEALTH);
            case CATEGORY_LIFESTYLE:
                return Arrays.asList(SUBCATEGORY_TRAVEL, SUBCATEGORY_FOOD, SUBCATEGORY_FASHION);
            case CATEGORY_BUSINESS:
                return Arrays.asList(SUBCATEGORY_FINANCE, SUBCATEGORY_ECONOMY);
            case CATEGORY_SPORTS:
                return Arrays.asList(SUBCATEGORY_FOOTBALL, SUBCATEGORY_BASKETBALL, SUBCATEGORY_TENNIS);
            case CATEGORY_NEWS:
                return Arrays.asList(SUBCATEGORY_WORLD, SUBCATEGORY_POLITICS, SUBCATEGORY_ECONOMY);
            default:
                return new ArrayList<>();
        }
    }

    private static List<Article> generateDummyArticles() {
        List<Article> articles = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Add Tech Articles - Android
        articles.add(createArticle(
                "tech1",
                "The Future of AI in Android Development",
                LOREM_IPSUM,
                "Alex Johnson",
                "ai_android.jpg",
                calendar.getTime(),
                1250,
                CATEGORY_TECH,
                SUBCATEGORY_ANDROID
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech2",
                "Android 14: New Features and Updates",
                LOREM_IPSUM,
                "Maria Garcia",
                "android_14.jpg",
                calendar.getTime(),
                980,
                CATEGORY_TECH,
                SUBCATEGORY_ANDROID
        ));

        // iOS Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech3",
                "iOS 17: What's New for Developers",
                LOREM_IPSUM,
                "James Wilson",
                "ios17.jpg",
                calendar.getTime(),
                1100,
                CATEGORY_TECH,
                SUBCATEGORY_IOS
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech4",
                "Building Your First SwiftUI App",
                LOREM_IPSUM,
                "Sarah Chen",
                "swiftui.jpg",
                calendar.getTime(),
                850,
                CATEGORY_TECH,
                SUBCATEGORY_IOS
        ));

        // Web Development Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech5",
                "Building Scalable Web Applications in 2023",
                LOREM_IPSUM,
                "David Kim",
                "web_dev.jpg",
                calendar.getTime(),
                1320,
                CATEGORY_TECH,
                SUBCATEGORY_WEB
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech6",
                "React vs Vue vs Angular in 2023",
                LOREM_IPSUM,
                "Emma Davis",
                "frameworks.jpg",
                calendar.getTime(),
                1500,
                CATEGORY_TECH,
                SUBCATEGORY_WEB
        ));

        // AI Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech7",
                "The Future of AI in Mobile Development",
                LOREM_IPSUM,
                "Alex Johnson",
                "ai_mobile.jpg",
                calendar.getTime(),
                2100,
                CATEGORY_TECH,
                SUBCATEGORY_AI
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "tech8",
                "Getting Started with Machine Learning",
                LOREM_IPSUM,
                "Robert Taylor",
                "ml_basics.jpg",
                calendar.getTime(),
                1750,
                CATEGORY_TECH,
                SUBCATEGORY_AI
        ));

        // Add Health Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "health1",
                "10 Essential Exercises for Home Workouts",
                LOREM_IPSUM,
                "Dr. Michael Brown",
                "home_workout.jpg",
                calendar.getTime(),
                1530,
                CATEGORY_HEALTH,
                SUBCATEGORY_FITNESS
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -2);
        articles.add(createArticle(
                "health2",
                "The Science of Intermittent Fasting",
                LOREM_IPSUM,
                "Dr. Emily Wilson",
                "nutrition.jpg",
                calendar.getTime(),
                2100,
                CATEGORY_HEALTH,
                SUBCATEGORY_NUTRITION
        ));

        // Add Sports Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "sport1",
                "Local Team Wins Championship in Overtime Thriller",
                LOREM_IPSUM,
                "John Sportsman",
                "football_championship.jpg",
                calendar.getTime(),
                3200,
                CATEGORY_SPORTS,
                SUBCATEGORY_FOOTBALL
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "sport2",
                "NBA Season Preview: Top Teams to Watch",
                LOREM_IPSUM,
                "Jane Hooper",
                "nba_preview.jpg",
                calendar.getTime(),
                2750,
                CATEGORY_SPORTS,
                SUBCATEGORY_BASKETBALL
        ));

        // Add News Articles
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "news1",
                "Global Leaders Sign Historic Climate Agreement",
                LOREM_IPSUM,
                "Global News Network",
                "climate_summit.jpg",
                calendar.getTime(),
                4100,
                CATEGORY_NEWS,
                SUBCATEGORY_WORLD
        ));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        articles.add(createArticle(
                "news2",
                "New Economic Policy Aims to Boost Local Businesses",
                LOREM_IPSUM,
                "Financial Times",
                "economic_policy.jpg",
                calendar.getTime(),
                1870,
                CATEGORY_NEWS,
                SUBCATEGORY_ECONOMY
        ));

        // Add more articles for better coverage...

        return articles;
    }

    /**
     * Gets popular articles, trying the API first and falling back to dummy data
     *
     * @param count    Maximum number of articles to return
     * @param callback Callback to receive the results asynchronously
     */
    public void getPopularArticles(int count, DataLoadListener callback) {
        // For this example, we'll just get all articles and sort by view count
        // In a real app, you might have a separate API endpoint for popular articles
        getAllArticles(new DataLoadListener() {
            @Override
            public void onDataLoaded(List<Article> articles) {
                if (articles != null) {
                    // Sort by view count (descending) and limit to count
                    articles.sort((a1, a2) -> Integer.compare(a2.getViewCount(), a1.getViewCount()));
                    List<Article> result = articles.subList(0, Math.min(count, articles.size()));
                    if (callback != null) {
                        callback.onDataLoaded(result);
                    }
                } else if (callback != null) {
                    callback.onError("Failed to load popular articles");
                }
            }

            @Override
            public void onError(String message) {
                if (callback != null) {
                    callback.onError(message);
                }
            }
        });
    }

    /**
     * Gets latest articles, trying the API first and falling back to dummy data
     *
     * @param count    Maximum number of articles to return
     * @param callback Callback to receive the results asynchronously
     */
    public void getLatestArticles(int count, DataLoadListener callback) {
        // For this example, we'll just get all articles and sort by date
        // In a real app, you might have a separate API endpoint for latest articles
        getAllArticles(new DataLoadListener() {
            @Override
            public void onDataLoaded(List<Article> articles) {
                if (articles != null) {
                    // Sort by publish date (newest first) and limit to count
                    articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()));
                    List<Article> result = articles.subList(0, Math.min(count, articles.size()));
                    if (callback != null) {
                        callback.onDataLoaded(result);
                    }
                } else if (callback != null) {
                    callback.onError("Failed to load latest articles");
                }
            }

            @Override
            public void onError(String message) {
                if (callback != null) {
                    callback.onError(message);
                }
            }
        });
    }

    /**
     * Gets all articles, trying the API first and falling back to dummy data
     *
     * @param callback Callback to receive the results asynchronously
     */
    private void getAllArticles(DataLoadListener callback) {
        if (!isViewModelInitialized) {
            // Fall back to dummy data if ViewModel isn't initialized
            List<Article> dummyArticles = getDummyAllArticles();
            if (callback != null) {
                callback.onDataLoaded(dummyArticles);
            }
            return;
        }

        // Try to get data from the API first
        articleViewModel.loadArticles(null, 1, 100); // Assuming 100 is a reasonable max
        articleViewModel.getArticlesLiveData().observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null && !articles.isEmpty()) {
                    if (callback != null) {
                        callback.onDataLoaded(articles);
                    }
                    articleViewModel.getArticlesLiveData().removeObserver(this);
                } else {
                    List<Article> dummyArticles = getDummyAllArticles();
                    if (callback != null) {
                        callback.onDataLoaded(dummyArticles);
                    }
                    articleViewModel.getArticlesLiveData().removeObserver(this);
                }
            }
        });

        // Set up error handling
        articleViewModel.getErrorLiveData().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && callback != null) {
                    List<Article> dummyArticles = getDummyAllArticles();
                    callback.onDataLoaded(dummyArticles);
                    articleViewModel.getErrorLiveData().removeObserver(this);
                }
            }
        });
    }

    /**
     * Gets all articles from dummy data (fallback method)
     */
    private List<Article> getDummyAllArticles() {
        return getDummyArticles();
    }

    private static Article createArticle(String id, String title, String content, String authorName,
                                         String imageName, Date date, int viewCount,
                                         String categoryId, String subcategoryId) {
        String imageUrl = "https://example.com/images/" + imageName;
        Article article = new Article(
                id,
                title,
                content,
                imageUrl,
                categoryId,
                subcategoryId,
                "author_" + id,
                authorName,
                "https://example.com/authors/" + authorName.toLowerCase().replace(" ", "_") + ".jpg",
                date,
                viewCount,
                viewCount / 10
        );

        return article;
    }
}