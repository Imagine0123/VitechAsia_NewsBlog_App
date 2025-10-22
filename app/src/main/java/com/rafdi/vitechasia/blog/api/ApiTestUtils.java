package com.rafdi.vitechasia.blog.api;

import android.content.Context;
import android.util.Log;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class for testing API implementations.
 * Provides methods to test both real and fake API services.
 */
public class ApiTestUtils {
    private static final String TAG = "ApiTestUtils";

    /**
     * Tests the ArticleApiService implementation by making sample requests.
     *
     * @param apiService The API service to test
     * @param callback Callback to receive test results
     */
    public static void testArticleApiService(ArticleApiService apiService, TestCallback callback) {
        Log.i(TAG, "Starting API service tests...");

        // Test 1: Get all articles
        apiService.getArticles(null, 1, 10).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "✅ Test 1 PASSED: Retrieved " + response.body().size() + " articles");
                    if (callback != null) {
                        callback.onTestResult("Get All Articles", true, "Retrieved " + response.body().size() + " articles");
                    }
                } else {
                    Log.e(TAG, "❌ Test 1 FAILED: " + response.message());
                    if (callback != null) {
                        callback.onTestResult("Get All Articles", false, response.message());
                    }
                }

                // Test 2: Get articles by category
                testCategoryFilter(apiService, callback);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.e(TAG, "❌ Test 1 FAILED: " + t.getMessage());
                if (callback != null) {
                    callback.onTestResult("Get All Articles", false, t.getMessage());
                }
                testCategoryFilter(apiService, callback);
            }
        });
    }

    /**
     * Tests category filtering functionality.
     */
    private static void testCategoryFilter(ArticleApiService apiService, TestCallback callback) {
        apiService.getArticles("tech", 1, 5).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body();
                    boolean allTechArticles = articles.stream().allMatch(article -> "tech".equalsIgnoreCase(article.getCategoryId()));

                    if (allTechArticles) {
                        Log.i(TAG, "✅ Test 2 PASSED: All " + articles.size() + " articles are tech articles");
                        if (callback != null) {
                            callback.onTestResult("Category Filter", true, "All " + articles.size() + " articles are tech articles");
                        }
                    } else {
                        Log.e(TAG, "❌ Test 2 FAILED: Some articles are not tech articles");
                        if (callback != null) {
                            callback.onTestResult("Category Filter", false, "Some articles are not tech articles");
                        }
                    }
                } else {
                    Log.e(TAG, "❌ Test 2 FAILED: " + response.message());
                    if (callback != null) {
                        callback.onTestResult("Category Filter", false, response.message());
                    }
                }

                // Test 3: Get single article
                testSingleArticle(apiService, callback);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.e(TAG, "❌ Test 2 FAILED: " + t.getMessage());
                if (callback != null) {
                    callback.onTestResult("Category Filter", false, t.getMessage());
                }
                testSingleArticle(apiService, callback);
            }
        });
    }

    /**
     * Tests single article retrieval.
     */
    private static void testSingleArticle(ArticleApiService apiService, TestCallback callback) {
        // Use a known article ID from the dummy data
        apiService.getArticleById("tech1").enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Article article = response.body();
                    Log.i(TAG, "✅ Test 3 PASSED: Retrieved article '" + article.getTitle() + "'");
                    if (callback != null) {
                        callback.onTestResult("Single Article", true, "Retrieved article: " + article.getTitle());
                    }
                } else {
                    Log.e(TAG, "❌ Test 3 FAILED: " + response.message());
                    if (callback != null) {
                        callback.onTestResult("Single Article", false, response.message());
                    }
                }

                // Test 4: Non-existent article
                testNonExistentArticle(apiService, callback);
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.e(TAG, "❌ Test 3 FAILED: " + t.getMessage());
                if (callback != null) {
                    callback.onTestResult("Single Article", false, t.getMessage());
                }
                testNonExistentArticle(apiService, callback);
            }
        });
    }

    /**
     * Tests behavior when requesting a non-existent article.
     */
    private static void testNonExistentArticle(ArticleApiService apiService, TestCallback callback) {
        apiService.getArticleById("nonexistent123").enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.i(TAG, "✅ Test 4 PASSED: Correctly handled non-existent article");
                    if (callback != null) {
                        callback.onTestResult("Non-existent Article", true, "Correctly returned null/error for non-existent article");
                        callback.onTestsComplete();
                    }
                } else {
                    Log.e(TAG, "❌ Test 4 FAILED: Should not return article for non-existent ID");
                    if (callback != null) {
                        callback.onTestResult("Non-existent Article", false, "Should not return article for non-existent ID");
                        callback.onTestsComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.i(TAG, "✅ Test 4 PASSED: Correctly handled non-existent article (error)");
                if (callback != null) {
                    callback.onTestResult("Non-existent Article", true, "Correctly returned error for non-existent article");
                    callback.onTestsComplete();
                }
            }
        });
    }

    /**
     * Starts the fake API server for testing.
     */
    public static boolean startTestServer(Context context) {
        return FakeApiServer.startServer(context, 8080);
    }

    /**
     * Stops the fake API server.
     */
    public static void stopTestServer() {
        FakeApiServer.stopServer();
    }

    /**
     * Callback interface for test results.
     */
    public interface TestCallback {
        void onTestResult(String testName, boolean success, String message);
        void onTestsComplete();
    }

    /**
     * Simple test callback implementation that logs results.
     */
    public static class LoggingTestCallback implements TestCallback {
        @Override
        public void onTestResult(String testName, boolean success, String message) {
            Log.i(TAG, String.format("%s: %s - %s",
                    success ? "✅" : "❌", testName, message));
        }

        @Override
        public void onTestsComplete() {
            Log.i(TAG, "=== API Tests Complete ===");
        }
    }
}
