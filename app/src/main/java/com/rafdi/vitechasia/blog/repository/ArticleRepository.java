package com.rafdi.vitechasia.blog.repository;

import androidx.lifecycle.MutableLiveData;

import com.rafdi.vitechasia.blog.api.ApiClient;
import com.rafdi.vitechasia.blog.api.ArticleApiService;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {
    private static ArticleRepository instance;
    private final ArticleApiService apiService;
    
    private ArticleRepository() {
        apiService = ApiClient.getArticleApiService();
    }
    
    public static synchronized ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepository();
        }
        return instance;
    }
    
    public void getArticles(String category, int page, int limit, final ArticleCallback callback) {
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
    
    public void getArticleById(String id, final SingleArticleCallback callback) {
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
    
    public interface ArticleCallback {
        void onSuccess(List<Article> articles);
        void onError(String message);
    }
    
    public interface SingleArticleCallback {
        void onSuccess(Article article);
        void onError(String message);
    }
}
