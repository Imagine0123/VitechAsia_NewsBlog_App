package com.rafdi.vitechasia.blog.api;

import android.content.Context;
import android.util.Log;

import com.rafdi.vitechasia.blog.BuildConfig;

/**
 * Factory class for creating API service instances.
 * Automatically switches between real and fake API implementations based on configuration.
 *
 * <p>This class provides a clean interface for getting API services while hiding
 * the complexity of switching between real and fake implementations.
 */
public class ApiServiceFactory {
    private static final String TAG = "ApiServiceFactory";

    /**
     * Gets an ArticleApiService instance based on the current configuration.
     * In debug builds, returns a fake implementation for testing.
     * In release builds, returns the real API implementation.
     *
     * @return ArticleApiService instance (real or fake)
     */
    public static ArticleApiService getArticleApiService() {
        if (ApiConfig.isFakeApiEnabled()) {
            Log.i(TAG, "Using FAKE ArticleApiService for testing");
            return new FakeArticleApiService();
        } else {
            Log.i(TAG, "Using REAL ArticleApiService");
            return ApiClient.getArticleApiService();
        }
    }

    /**
     * Gets an ArticleApiService instance with explicit mode selection.
     * Useful for testing different scenarios in the same build.
     *
     * @param useFakeApi true for fake API, false for real API
     * @return ArticleApiService instance
     */
    public static ArticleApiService getArticleApiService(boolean useFakeApi) {
        if (useFakeApi) {
            Log.i(TAG, "Using FAKE ArticleApiService (explicit request)");
            return new FakeArticleApiService();
        } else {
            Log.i(TAG, "Using REAL ArticleApiService (explicit request)");
            return ApiClient.getArticleApiService();
        }
    }

    /**
     * Starts the fake API server for testing.
     * The server will run on localhost and provide HTTP endpoints.
     *
     * @param context Android context
     * @param port Port number (8080 is recommended for emulator)
     * @return true if server started successfully
     */
    public static boolean startFakeApiServer(Context context, int port) {
        return FakeApiServer.startServer(context, port);
    }

    /**
     * Stops the fake API server.
     */
    public static void stopFakeApiServer() {
        FakeApiServer.stopServer();
    }

    /**
     * Checks if the fake API server is currently running.
     */
    public static boolean isFakeApiServerRunning() {
        return FakeApiServer.isServerRunning();
    }

    /**
     * Gets the current API configuration for debugging.
     */
    public static void logCurrentConfiguration() {
        Log.d(TAG, "=== API Configuration ===");
        Log.d(TAG, "Fake API Enabled: " + ApiConfig.isFakeApiEnabled());
        Log.d(TAG, "Base URL: " + ApiConfig.getBaseUrl());
        Log.d(TAG, "Fake Server Running: " + isFakeApiServerRunning());
        Log.d(TAG, "Build Type: " + (BuildConfig.DEBUG ? "DEBUG" : "RELEASE"));
        Log.d(TAG, "========================");
    }
}
