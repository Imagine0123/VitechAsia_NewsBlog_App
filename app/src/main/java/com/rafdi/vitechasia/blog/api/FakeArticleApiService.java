package com.rafdi.vitechasia.blog.api;

import android.util.Log;

import com.google.gson.Gson;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fake implementation of ArticleApiService for testing purposes.
 * Provides realistic API responses with simulated network delays and occasional errors.
 */
public class FakeArticleApiService implements ArticleApiService {
    private static final String TAG = "FakeArticleApiService";
    private final Random random = new Random();
    private final Gson gson = new Gson();

    @Override
    public Call<List<Article>> getArticles(String category, int page, int limit) {
        return new FakeCall<>(() -> {
            simulateNetworkDelay();

            // Simulate occasional errors
            if (shouldSimulateError()) {
                throw new RuntimeException("Simulated network error");
            }

            List<Article> allArticles = DataHandler.getDummyArticles();
            List<Article> filteredArticles = new ArrayList<>();

            // Filter by category if provided
            if (category != null && !category.trim().isEmpty()) {
                for (Article article : allArticles) {
                    if (category.equalsIgnoreCase(article.getCategoryId())) {
                        filteredArticles.add(article);
                    }
                }
            } else {
                filteredArticles.addAll(allArticles);
            }

            // Sort by publish date (newest first)
            filteredArticles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()));

            // Apply pagination
            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, filteredArticles.size());

            if (startIndex >= filteredArticles.size()) {
                return new ArrayList<>(); // Empty page
            }

            List<Article> result = filteredArticles.subList(startIndex, endIndex);

            Log.d(TAG, String.format("Returning %d articles for category '%s', page %d, limit %d",
                    result.size(), category, page, limit));

            return result;
        });
    }

    @Override
    public Call<Article> getArticleById(String id) {
        return new FakeCall<>(() -> {
            simulateNetworkDelay();

            // Simulate occasional errors
            if (shouldSimulateError()) {
                throw new RuntimeException("Simulated network error");
            }

            List<Article> allArticles = DataHandler.getDummyArticles();
            for (Article article : allArticles) {
                if (id.equals(article.getId())) {
                    Log.d(TAG, "Found article: " + article.getTitle());
                    return article;
                }
            }

            Log.d(TAG, "Article not found with id: " + id);
            return null; // Article not found
        });
    }

    /**
     * Simulates realistic network delay between API calls.
     */
    private void simulateNetworkDelay() {
        try {
            int delay = ApiConfig.FAKE_API_DELAY_MIN +
                    random.nextInt(ApiConfig.FAKE_API_DELAY_MAX - ApiConfig.FAKE_API_DELAY_MIN);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Simulates occasional network errors based on configured error rate.
     */
    private boolean shouldSimulateError() {
        return random.nextDouble() < ApiConfig.FAKE_API_ERROR_RATE;
    }

    /**
     * Fake Call implementation that mimics Retrofit's Call interface.
     */
    private static class FakeCall<T> implements Call<T> {
        private final FakeCallable<T> callable;
        private boolean executed = false;
        private T result;
        private Exception error;

        public FakeCall(FakeCallable<T> callable) {
            this.callable = callable;
        }

        @Override
        public Response<T> execute() {
            executed = true;
            try {
                result = callable.call();
                return Response.success(result);
            } catch (Exception e) {
                error = e;
                // Create error response with proper body
                ResponseBody errorBody = ResponseBody.create(
                        MediaType.get("application/json"),
                        "{\"error\":\"" + e.getMessage() + "\"}"
                );
                return Response.error(500, errorBody);
            }
        }

        @Override
        public void enqueue(Callback<T> callback) {
            // Simulate async execution on background thread
            new Thread(() -> {
                Response<T> response = execute();
                if (response.isSuccessful()) {
                    callback.onResponse(this, response);
                } else {
                    callback.onFailure(this, new RuntimeException("API call failed"));
                }
            }).start();
        }

        @Override
        public boolean isExecuted() {
            return executed;
        }

        @Override
        public void cancel() {
            // No-op for fake implementation
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<T> clone() {
            return new FakeCall<>(callable);
        }

        @Override
        public okhttp3.Request request() {
            // Create a fake request for compatibility
            return new okhttp3.Request.Builder()
                    .url("http://fake-api.local/articles")
                    .build();
        }

        @Override
        public Call<T> timeout(long timeout, java.util.concurrent.TimeUnit unit) {
            // Return this call with timeout - fake implementation
            return this;
        }
    }

    /**
     * Interface for fake API callables.
     */
    private interface FakeCallable<T> {
        T call() throws Exception;
    }
}
