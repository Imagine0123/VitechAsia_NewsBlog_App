package com.rafdi.vitechasia.blog.api;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApiService {
    @GET("articles")
    Call<List<Article>> getArticles(
            @Query("category") String category,
            @Query("page") int page,
            @Query("limit") int limit
    );
    
    @GET("articles/{id}")
    Call<Article> getArticleById(@Path("id") String id);
}
