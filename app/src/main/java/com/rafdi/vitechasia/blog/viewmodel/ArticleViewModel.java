package com.rafdi.vitechasia.blog.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.repository.ArticleRepository;

import java.util.List;

/**
 * ViewModel class that manages and provides data for the UI components.
 * Acts as a communication center between the {@link ArticleRepository} and the UI.
 * 
 * This class holds and manages UI-related data in a lifecycle-conscious way, allowing data to
 * survive configuration changes such as screen rotations. It provides LiveData that can be observed
 * by UI components to react to data changes.
 */

public class ArticleViewModel extends AndroidViewModel {
    private final ArticleRepository repository;
    private final MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Article> articleLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        this.repository = ArticleRepository.getInstance(application);
    }

    /**
 * Loads a list of articles with the specified category, page, and limit.
 * Updates the corresponding LiveData objects based on the result.
 *
 * @param category The category to filter by, or null for all categories
 * @param page The page number for pagination (starting from 1)
 * @param limit The maximum number of articles to return per page
 */
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

    /**
 * Loads a single article by its ID.
 * Updates the corresponding LiveData objects based on the result.
 *
 * @param id The ID of the article to load
 */
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

    /**
 * Returns the LiveData containing the list of articles.
 * 
 * @return LiveData containing a list of articles
 */
public LiveData<List<Article>> getArticlesLiveData() {
        return articlesLiveData;
    }

    /**
 * Returns the LiveData containing a single article.
 * 
 * @return LiveData containing a single article
 */
public LiveData<Article> getArticleLiveData() {
        return articleLiveData;
    }

    /**
 * Returns the LiveData containing error messages.
 * 
 * @return LiveData containing error messages as Strings
 */
public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
 * Returns the LiveData indicating the loading state.
 * 
 * @return LiveData containing a boolean indicating if data is being loaded
 */
public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
