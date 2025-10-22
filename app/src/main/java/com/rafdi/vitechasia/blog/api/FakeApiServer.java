package com.rafdi.vitechasia.blog.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import fi.iki.elonen.NanoHTTPD;

/**
 * Simple HTTP server implementation for testing API endpoints.
 * Runs on localhost and provides RESTful endpoints that mimic the real API.
 *
 * Usage:
 * 1. Start the server: FakeApiServer.startServer(context, 8080)
 * 2. Access endpoints: http://10.0.2.2:8080/api/v1/articles
 * 3. Stop the server: FakeApiServer.stopServer()
 */
public class FakeApiServer extends NanoHTTPD {
    private static final String TAG = "FakeApiServer";
    private static FakeApiServer instance;
    private final Context context;
    private final Gson gson;
    private final Random random = new Random();
    private volatile boolean isRunning = false;

    /**
     * Private constructor for singleton pattern.
     */
    private FakeApiServer(Context context, int port) {
        super(port);
        this.context = context.getApplicationContext();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Log.i(TAG, "Fake API Server initialized on port " + port);
    }

    /**
     * Starts the fake API server.
     *
     * @param context Android context
     * @param port Port to run the server on (use 8080 for emulator)
     * @return true if server started successfully, false otherwise
     */
    public static boolean startServer(Context context, int port) {
        if (instance != null && instance.isRunning) {
            Log.w(TAG, "Server is already running");
            return true;
        }

        // Validate port
        if (port < 1024 || port > 65535) {
            Log.e(TAG, "Invalid port number: " + port + ". Use a port between 1024 and 65535.");
            return false;
        }

        try {
            instance = new FakeApiServer(context, port);
            instance.start();
            instance.isRunning = true;
            Log.i(TAG, "Fake API Server started successfully on port " + port);
            Log.i(TAG, "Test the API at: http://10.0.2.2:" + port + "/api/v1/articles");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to start fake API server on port " + port, e);
            instance = null;
            return false;
        }

    /**
     * Checks if the fake API server is currently running.
     */
    public static boolean isServerRunning() {
        return instance != null && instance.isRunning;
    }

    @Override
    public void stop() {
        super.stop();
        isRunning = false;
        Log.i(TAG, "Fake API Server stopped");
    }

    /**
     * Gets CORS headers for browser testing.
     */
    private Map<String, String> getCORSHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public Response serve(IHTTPSession session) {
        // Check if server should still be running
        if (!isRunning) {
            return createJsonResponse(Response.Status.SERVICE_UNAVAILABLE, "{\"error\":\"Server is shutting down\"}");
        }

        String uri = session.getUri();
        Method method = session.getMethod();

        Log.d(TAG, "Request: " + method + " " + uri);

        // Handle preflight requests
        if (method == Method.OPTIONS) {
            return createJsonResponse(Response.Status.OK, "{}");
        }

        // Simulate occasional network delays
        simulateDelay();

        try {
            // Route requests to appropriate handlers
            if (uri.startsWith("/api/v1/articles")) {
                return handleArticlesRequest(session);
            } else {
                return createJsonResponse(Response.Status.NOT_FOUND, "{\"error\":\"Endpoint not found\"}");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling request: " + uri, e);
            return createJsonResponse(Response.Status.INTERNAL_ERROR, "{\"error\":\"Internal server error\"}");
        }
    }

    private Response handleArticlesRequest(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, List<String>> params = session.getParameters();

        try {
            if (uri.equals("/api/v1/articles") || uri.startsWith("/api/v1/articles?")) {
                return handleGetArticles(params);
            } else if (uri.matches("/api/v1/articles/[^/]+")) {
                String articleId = extractArticleId(uri);
                return handleGetArticleById(articleId);
            } else {
                return createJsonResponse(Response.Status.NOT_FOUND, "{\"error\":\"Article endpoint not found\"}");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling articles request", e);
            return createJsonResponse(Response.Status.INTERNAL_ERROR, "{\"error\":\"Failed to process articles request\"}");
        }
    }

    /**
     * Handles GET /api/v1/articles requests with filtering and pagination.
     */
    private Response handleGetArticles(Map<String, List<String>> params, Map<String, String> headers) {
        // Get articles from DataHandler (with fallback protection)
        List<Article> allArticles;
        try {
            allArticles = DataHandler.getDummyArticles();
        } catch (Exception e) {
            Log.e(TAG, "Error getting articles from DataHandler", e);
            allArticles = new ArrayList<>();
        }

        if (allArticles == null) {
            allArticles = new ArrayList<>();
        }

        List<Article> filteredArticles = new ArrayList<>();

        // Filter by category if provided
        List<String> categories = params.get("category");
        if (categories != null && !categories.isEmpty()) {
            String categoryFilter = categories.get(0);
            if (categoryFilter != null && !categoryFilter.trim().isEmpty()) {
                for (Article article : allArticles) {
                    if (article != null && categoryFilter.equalsIgnoreCase(article.getCategoryId())) {
                        filteredArticles.add(article);
                    }
                }
            }
        } else {
            filteredArticles.addAll(allArticles);
        }

        // Sort by publish date (newest first)
        filteredArticles.sort((a1, a2) -> {
            if (a1 == null || a2 == null) return 0;
            if (a1.getPublishDate() == null || a2.getPublishDate() == null) return 0;
            return a2.getPublishDate().compareTo(a1.getPublishDate());
        });

        // Apply pagination
        int page = 1;
        int limit = 20;

        List<String> pageParams = params.get("page");
        if (pageParams != null && !pageParams.isEmpty()) {
            try {
                page = Integer.parseInt(pageParams.get(0));
            } catch (NumberFormatException e) {
                Log.w(TAG, "Invalid page parameter: " + pageParams.get(0));
            }
        }

        List<String> limitParams = params.get("limit");
        if (limitParams != null && !limitParams.isEmpty()) {
            try {
                limit = Integer.parseInt(limitParams.get(0));
            } catch (NumberFormatException e) {
                Log.w(TAG, "Invalid limit parameter: " + limitParams.get(0));
            }
        }

        int startIndex = (page - 1) * limit;
        int endIndex = Math.min(startIndex + limit, filteredArticles.size());

        List<Article> result;
        if (startIndex >= filteredArticles.size()) {
            result = new ArrayList<>(); // Empty page
        } else {
            result = filteredArticles.subList(startIndex, endIndex);
        }

        // Create response with metadata
        Map<String, Object> response = new HashMap<>();
        response.put("articles", result);
        response.put("pagination", createPaginationInfo(page, limit, filteredArticles.size(), result.size()));

        String jsonResponse = gson.toJson(response);
        Log.d(TAG, String.format("Returning %d articles, page %d/%d",
                result.size(), page, (filteredArticles.size() + limit - 1) / limit));

        return createJsonResponse(Response.Status.OK, jsonResponse);
    }

    private Response handleGetArticleById(String articleId, Map<String, String> headers) {
        if (articleId == null || articleId.trim().isEmpty()) {
            Log.d(TAG, "Invalid article ID: " + articleId);
            return createJsonResponse(Response.Status.BAD_REQUEST, "{\"error\":\"Invalid article ID\"}");
        }

        List<Article> allArticles;
        try {
            allArticles = DataHandler.getDummyArticles();
        } catch (Exception e) {
            Log.e(TAG, "Error getting articles from DataHandler", e);
            return createJsonResponse(Response.Status.INTERNAL_ERROR, "{\"error\":\"Failed to retrieve articles\"}");
        }

        if (allArticles == null) {
            allArticles = new ArrayList<>();
        }

        for (Article article : allArticles) {
            if (article != null && articleId.equals(article.getId())) {
                String jsonResponse = gson.toJson(article);
                Log.d(TAG, "Found article: " + article.getTitle());
                return createJsonResponse(Response.Status.OK, jsonResponse);
            }
        }

        Log.d(TAG, "Article not found: " + articleId);
        return createJsonResponse(Response.Status.NOT_FOUND, "{\"error\":\"Article not found\"}");
    }

    /**
     * Creates a JSON response for NanoHTTPD with proper format.
     */
    private Response createJsonResponse(Response.Status status, String json) {
        InputStream inputStream = createJsonInputStream(json);
        try {
            return newFixedLengthResponse(status, "application/json", inputStream, inputStream.available(), getCORSHeaders());
        } catch (IOException e) {
            Log.e(TAG, "Error creating JSON response", e);
            // Fallback response
            return newFixedLengthResponse(status, "application/json", createJsonInputStream("{\"error\":\"Response creation failed\"}"), 30, getCORSHeaders());
        }
    }

    /**
     * Extracts article ID from URI like /api/v1/articles/{id}.
     */
    private String extractArticleId(String uri) {
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }

    /**
     * Creates pagination information map.
     */
    private Map<String, Integer> createPaginationInfo(int page, int limit, int total, int count) {
        Map<String, Integer> pagination = new HashMap<>();
        pagination.put("page", page);
        pagination.put("limit", limit);
        pagination.put("total", total);
        pagination.put("count", count);
        return pagination;
    }

    /**
     * Simulates realistic network delay.
     */
    private void simulateDelay() {
        if (random.nextDouble() < 0.1) { // 10% chance of delay
            try {
                Thread.sleep(100 + random.nextInt(400)); // 100-500ms delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
