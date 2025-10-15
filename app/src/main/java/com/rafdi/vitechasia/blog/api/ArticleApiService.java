package com.rafdi.vitechasia.blog.api;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit service interface for the Article API endpoints.
 * Defines the HTTP methods and endpoints for interacting with the article resources.
 * 
 * <p>This interface is used by Retrofit to create the actual implementation of the API calls.
 * All methods return {@link Call} objects that can be executed asynchronously.
 */

public interface ArticleApiService {
    /**
     * Fetches a paginated list of articles, optionally filtered by category.
     *
     * @param category Optional category to filter articles. Can be null to get all articles.
     * @param page The page number for pagination (starting from 1).
     * @param limit The maximum number of articles to return per page.
     * @return A {@link Call} that represents the HTTP request for a list of articles.
     */
    @GET("articles")
    Call<List<Article>> getArticles(
            @Query("category") String category,
            @Query("page") int page,
            @Query("limit") int limit
    );
    
    /**
     * Fetches a single article by its unique identifier.
     *
     * @param id The unique identifier of the article to retrieve.
     * @return A {@link Call} that represents the HTTP request for a single article.
     */
    @GET("articles/{id}")
    Call<Article> getArticleById(@Path("id") String id);
}
