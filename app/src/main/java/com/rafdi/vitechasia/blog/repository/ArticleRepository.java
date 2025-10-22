package com.rafdi.vitechasia.blog.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.api.ApiServiceFactory;
import com.rafdi.vitechasia.blog.api.ArticleApiService;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class that handles data operations for articles.
 * Serves as a single source of truth for all article-related data in the application.
 * 
 * <p>This class follows the repository pattern to abstract the data sources from the rest of the app.
 * It handles the communication between the API service and the ViewModel, and can be extended
 * to include local data caching in the future.
 * 
 * <p>Use {@link #getInstance(Context)} to get the singleton instance of this class.
 */

public class ArticleRepository {
    private static volatile ArticleRepository instance;
    private final ArticleApiService apiService;
    private final Context context;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the API service using {@link ApiServiceFactory}.
     *
     * @param context The application context for checking network connectivity
     */
    private ArticleRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiServiceFactory.getArticleApiService();
    }
    
    /**
     * Returns the singleton instance of ArticleRepository.
     * 
     * @param context The application context (will use application context internally)
     * @return The singleton instance of ArticleRepository
     */
    public static ArticleRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ArticleRepository.class) {
                if (instance == null) {
                    instance = new ArticleRepository(context);
                }
            }
        }
        return instance;
    }

    /**
     * Fetches a list of articles from the API with pagination and optional category filtering.
     * Checks for network connectivity before making the API call.
     * 
     * @param category The category to filter by, or null for all categories
     * @param page The page number for pagination (starting from 1)
     * @param limit The maximum number of articles to return per page
     * @param callback The callback to handle the response or error
     */
    public void getArticles(String category, int page, int limit, final ArticleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            if (callback != null) {
                callback.onError(context.getString(R.string.error_no_internet_connection));
            }
            return;
        }
        apiService.getArticles(category, page, limit).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.message());
                }
            }
            
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Fetches a single article by its ID from the API.
     * Checks for network connectivity before making the API call.
     * 
     * @param id The ID of the article to fetch
     * @param callback The callback to handle the response or error
     */
    public void getArticleById(String id, final SingleArticleCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            if (callback != null) {
                callback.onError(context.getString(R.string.error_no_internet_connection));
            }
            return;
        }
        apiService.getArticleById(id).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.message());
                }
            }
            
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Callback interface for handling article list responses.
     */
    public interface ArticleCallback {
        void onSuccess(List<Article> articles);
        void onError(String message);
    }
    
    /**
     * Callback interface for handling single article responses.
     */
    public interface SingleArticleCallback {
        void onSuccess(Article article);
        void onError(String message);
    }
}
