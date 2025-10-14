package com.rafdi.vitechasia.blog.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.repository.ArticleRepository;

import java.util.List;

public class ArticleViewModel extends ViewModel {
    private final ArticleRepository repository;
    private final MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Article> articleLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public ArticleViewModel() {
        this.repository = ArticleRepository.getInstance();
    }

    public void loadArticles(String category, int page, int limit) {
        loadingLiveData.setValue(true);
        repository.getArticles(category, page, limit, new ArticleRepository.ArticleCallback() {
            @Override
            public void onSuccess(List<Article> articles) {
                loadingLiveData.postValue(false);
                articlesLiveData.postValue(articles);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(message);
            }
        });
    }

    public void loadArticleById(String id) {
        loadingLiveData.setValue(true);
        repository.getArticleById(id, new ArticleRepository.SingleArticleCallback() {
            @Override
            public void onSuccess(Article article) {
                loadingLiveData.postValue(false);
                articleLiveData.postValue(article);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(message);
            }
        });
    }

    public LiveData<List<Article>> getArticlesLiveData() {
        return articlesLiveData;
    }

    public LiveData<Article> getArticleLiveData() {
        return articleLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
