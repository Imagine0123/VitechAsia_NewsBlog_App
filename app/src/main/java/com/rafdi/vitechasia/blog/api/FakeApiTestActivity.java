package com.rafdi.vitechasia.blog.api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rafdi.vitechasia.blog.R;

/**
 * Test activity for demonstrating and testing the fake API implementation.
 * This activity provides buttons to test various API endpoints and scenarios.
 *
 * <p>To use this activity, add it to your AndroidManifest.xml and start it
 * from your main activity or navigation menu.
 */
public class FakeApiTestActivity extends AppCompatActivity {
    private static final String TAG = "FakeApiTestActivity";

    private TextView statusTextView;
    private TextView resultsTextView;
    private Button testFakeApiButton;
    private Button testRealApiButton;
    private Button startServerButton;
    private Button stopServerButton;
    private Button runFullTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_api_test);

        initializeViews();
        setupClickListeners();
        updateStatus();
    }

    private void initializeViews() {
        statusTextView = findViewById(R.id.statusTextView);
        resultsTextView = findViewById(R.id.resultsTextView);
        testFakeApiButton = findViewById(R.id.testFakeApiButton);
        testRealApiButton = findViewById(R.id.testRealApiButton);
        startServerButton = findViewById(R.id.startServerButton);
        stopServerButton = findViewById(R.id.stopServerButton);
        runFullTestButton = findViewById(R.id.runFullTestButton);
    }

    private void setupClickListeners() {
        testFakeApiButton.setOnClickListener(this::testFakeApi);
        testRealApiButton.setOnClickListener(this::testRealApi);
        startServerButton.setOnClickListener(this::startServer);
        stopServerButton.setOnClickListener(this::stopServer);
        runFullTestButton.setOnClickListener(this::runFullTest);
    }

    private void testFakeApi(View view) {
        appendResult("🧪 Testing FAKE API...");
        ArticleApiService fakeApi = ApiServiceFactory.getArticleApiService(true);

        ApiTestUtils.testArticleApiService(fakeApi, new ApiTestUtils.LoggingTestCallback() {
            @Override
            public void onTestResult(String testName, boolean success, String message) {
                super.onTestResult(testName, success, message);
                runOnUiThread(() -> appendResult("  " + testName + ": " + message));
            }

            @Override
            public void onTestsComplete() {
                super.onTestsComplete();
                runOnUiThread(() -> appendResult("✅ Fake API tests completed!"));
            }
        });
    }

    private void testRealApi(View view) {
        appendResult("🌐 Testing REAL API...");
        ArticleApiService realApi = ApiServiceFactory.getArticleApiService(false);

        ApiTestUtils.testArticleApiService(realApi, new ApiTestUtils.LoggingTestCallback() {
            @Override
            public void onTestResult(String testName, boolean success, String message) {
                super.onTestResult(testName, success, message);
                runOnUiThread(() -> appendResult("  " + testName + ": " + message));
            }

            @Override
            public void onTestsComplete() {
                super.onTestsComplete();
                runOnUiThread(() -> appendResult("✅ Real API tests completed!"));
            }
        });
    }

    private void startServer(View view) {
        appendResult("🚀 Starting fake API server...");
        boolean started = ApiServiceFactory.startFakeApiServer(this, 8080);

        if (started) {
            appendResult("✅ Server started on http://10.0.2.2:8080");
            appendResult("📱 Test endpoints:");
            appendResult("  GET http://10.0.2.2:8080/api/v1/articles");
            appendResult("  GET http://10.0.2.2:8080/api/v1/articles?category=tech&page=1&limit=5");
            appendResult("  GET http://10.0.2.2:8080/api/v1/articles/tech1");
        } else {
            appendResult("❌ Failed to start server");
        }

        updateStatus();
    }

    private void stopServer(View view) {
        appendResult("⏹️ Stopping fake API server...");
        ApiServiceFactory.stopFakeApiServer();
        appendResult("✅ Server stopped");
        updateStatus();
    }

    private void runFullTest(View view) {
        appendResult("🔬 Running comprehensive API tests...");
        ApiServiceFactory.logCurrentConfiguration();
        appendResult("");

        // Test both fake and real APIs
        testFakeApi(null);
        // Add delay before testing real API
        resultsTextView.postDelayed(() -> testRealApi(null), 3000);
    }

    private void updateStatus() {
        String status = "=== API Status ===\n";
        status += "Fake API: " + (ApiConfig.isFakeApiEnabled() ? "ENABLED" : "DISABLED") + "\n";
        status += "Server Running: " + (ApiServiceFactory.isFakeApiServerRunning() ? "YES" : "NO") + "\n";
        status += "Base URL: " + ApiConfig.getBaseUrl() + "\n";
        status += "================";

        statusTextView.setText(status);
    }

    private void appendResult(String message) {
        runOnUiThread(() -> {
            String current = resultsTextView.getText().toString();
            String timestamp = java.text.SimpleDateFormat.getTimeInstance().format(new java.util.Date());
            resultsTextView.setText(current + "\n[" + timestamp + "] " + message);

            // Auto-scroll to bottom
            resultsTextView.post(() -> {
                int scrollAmount = resultsTextView.getLayout().getLineTop(resultsTextView.getLineCount()) - resultsTextView.getHeight();
                if (scrollAmount > 0) {
                    resultsTextView.scrollTo(0, scrollAmount);
                } else {
                    resultsTextView.scrollTo(0, 0);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up server when activity is destroyed
        ApiServiceFactory.stopFakeApiServer();
    }
}
