package com.rafdi.vitechasia.blog.api;

import com.rafdi.vitechasia.blog.BuildConfig;

/**
 * Configuration class for API modes.
 * Allows switching between real API and fake API for testing.
 */
public class ApiConfig {
    private static final String TAG = "ApiConfig";

    // Use BuildConfig for automatic debug/release switching
    public static final boolean USE_FAKE_API = BuildConfig.USE_FAKE_API;

    // Fake API server configuration
    public static final String FAKE_API_BASE_URL = "http://10.0.2.2:8080/api/v1/";
    public static final String REAL_API_BASE_URL = "https://your-api-url.com/api/v1/";
    public static final int FAKE_API_DELAY_MIN = 100; // ms
    public static final int FAKE_API_DELAY_MAX = 800; // ms
    public static final double FAKE_API_ERROR_RATE = 0.05; // 5% error rate

    /**
     * Gets the appropriate base URL based on the current configuration.
     */
    public static String getBaseUrl() {
        return USE_FAKE_API ? FAKE_API_BASE_URL : REAL_API_BASE_URL;
    }

    /**
     * Checks if fake API mode is enabled.
     */
    public static boolean isFakeApiEnabled() {
        return USE_FAKE_API;
    }

    /**
     * Logs the current API configuration for debugging.
     */
    public static void logConfiguration() {
        android.util.Log.d(TAG, "API Configuration:");
        android.util.Log.d(TAG, "- Using fake API: " + USE_FAKE_API);
        android.util.Log.d(TAG, "- Base URL: " + getBaseUrl());
        android.util.Log.d(TAG, "- Error rate: " + (FAKE_API_ERROR_RATE * 100) + "%");
        android.util.Log.d(TAG, "- Delay: " + FAKE_API_DELAY_MIN + "-" + FAKE_API_DELAY_MAX + "ms");
    }
}
