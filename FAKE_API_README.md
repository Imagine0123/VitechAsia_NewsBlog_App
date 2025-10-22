# Fake API Implementation for Testing

This implementation provides a comprehensive fake API system for testing your Android app without requiring a real backend server. The system automatically switches between real and fake APIs based on your build configuration.

## 🚀 Features

### ✅ **Automatic Mode Switching**
- **Debug builds**: Uses fake API for safe testing
- **Release builds**: Uses real API for production
- **Manual override**: Force fake or real API in any build

### ✅ **Realistic API Simulation**
- **Network delays**: 100-800ms random delays
- **Error simulation**: 5% random error rate (configurable)
- **Pagination support**: Full pagination with filtering
- **CORS support**: Test endpoints in browser

### ✅ **HTTP Server for Testing**
- **Local server**: Runs on `http://10.0.2.2:8080` (emulator)
- **REST endpoints**: Full REST API simulation
- **JSON responses**: Proper JSON formatting with metadata
- **Browser testing**: Access endpoints directly in browser

### ✅ **Comprehensive Testing Tools**
- **Automated tests**: Test all API endpoints
- **Test activity**: Visual testing interface
- **Logging**: Detailed test results and debugging
- **Configuration display**: See current API settings

## 📁 Files Created

```
app/src/main/java/com/rafdi/vitechasia/blog/api/
├── ApiConfig.java              # Configuration settings
├── ApiServiceFactory.java      # Factory for API services
├── FakeArticleApiService.java  # Fake API implementation
├── FakeApiServer.java          # HTTP server for testing
├── FakeApiTestActivity.java    # Test interface
└── ApiTestUtils.java           # Testing utilities

app/src/main/res/layout/
└── activity_fake_api_test.xml  # Test activity layout
```

## 🔧 Setup Instructions

// Fake API Server (for testing)
implementation('org.nanohttpd:nanohttpd:2.3.1')

### **2. Configure API URLs**
Update your `ApiConfig.java`:
```java
// Fake API (emulator testing)
public static final String FAKE_API_BASE_URL = "http://10.0.2.2:8080/api/v1/";

// Real API (production)
public static final String REAL_API_BASE_URL = "https://your-api-url.com/api/v1/";
```

### **3. Build Configuration**
The system automatically switches based on your build type:
```kotlin
// In build.gradle.kts
buildTypes {
    debug {
        buildConfigField("boolean", "USE_FAKE_API", "true")
```java
// The system automatically chooses fake or real API
ArticleApiService apiService = ApiServiceFactory.getArticleApiService();

// This will use fake API in debug builds, real API in release builds
apiService.getArticles("tech", 1, 10).enqueue(callback);
```

### **Manual Override**
```java
// Force fake API (for testing)
// Force real API (for production testing)
ArticleApiService realApi = ApiServiceFactory.getArticleApiService(false);

### **4. Test the Implementation**
```java
// Start the server
boolean started = ApiServiceFactory.startFakeApiServer(context, 8080);

// Test in browser:
// http://10.0.2.2:8080/api/v1/articles
// http://10.0.2.2:8080/api/v1/articles?category=tech&page=1&limit=5
// http://10.0.2.2:8080/api/v1/articles/tech1

// Stop when done
ApiServiceFactory.stopFakeApiServer();
```

### **Run Automated Tests**
```java
// Test the current API implementation
ArticleApiService apiService = ApiServiceFactory.getArticleApiService();

ApiTestUtils.testArticleApiService(apiService, new ApiTestUtils.TestCallback() {
    @Override
    public void onTestResult(String testName, boolean success, String message) {
        Log.d(TAG, testName + ": " + message);
    }

    @Override
    public void onTestsComplete() {
        Log.d(TAG, "All tests completed!");
    }
});
```

## 🌐 HTTP API Endpoints

When the fake server is running, you can test these endpoints in your browser:

### **Get All Articles**
```
GET http://10.0.2.2:8080/api/v1/articles
```

### **Get Articles by Category**
```
GET http://10.0.2.2:8080/api/v1/articles?category=tech&page=1&limit=5
```

### **Get Single Article**
```
GET http://10.0.2.2:8080/api/v1/articles/tech1
```

### **Response Format**
```json
{
  "articles": [
    {
      "id": "tech1",
      "title": "The Future of AI in Android Development",
      "content": "Lorem ipsum...",
      "categoryId": "tech",
      "subcategoryId": "android",
      "authorName": "Alex Johnson",
      "publishDate": "2024-01-15T10:30:00.000Z",
      "viewCount": 1250,
      "likeCount": 125
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 10,
    "total": 25,
    "count": 10
  }
}
```

## 🧪 Testing Interface

### **Start Test Activity**
Add this activity to your `AndroidManifest.xml`:
```xml
<activity
    android:name=".api.FakeApiTestActivity"
    android:label="API Testing"
    android:exported="true" />
```

Then start it from your app:
```java
Intent intent = new Intent(this, FakeApiTestActivity.class);
startActivity(intent);
```

### **Test Activity Features**
- **Status Display**: Shows current API configuration
- **Test Buttons**: Test fake and real APIs separately
- **Server Controls**: Start/stop the HTTP server
- **Full Test Suite**: Run comprehensive tests
- **Live Results**: Real-time test output

## ⚙️ Configuration Options

### **Network Simulation**
```java
```java
// Log current configuration
ApiServiceFactory.logCurrentConfiguration();

// Enable HTTP logging (already configured in ApiClient)
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);
```

### **Check API Status**
```java
Log.d(TAG, "Fake API Enabled: " + ApiConfig.isFakeApiEnabled());
Log.d(TAG, "Server Running: " + ApiServiceFactory.isFakeApiServerRunning());
Log.d(TAG, "Base URL: " + ApiConfig.getBaseUrl());
```

## 🚨 Important Notes

### **Emulator vs Device**
- **Emulator**: Use `10.0.2.2` as localhost
- **Physical device**: Use your computer's IP address

### **Network Security**
- The fake server only runs on localhost
- No external network access
- Safe for development and testing

### **Performance**
- Fake API adds realistic delays for testing
- HTTP server runs in background thread
- Minimal impact on app performance

## 🎉 Benefits

### **For Developers**
- ✅ **No backend required** for development
- ✅ **Fast testing** without network calls
- ✅ **Predictable responses** for UI testing
- ✅ **Error simulation** for edge case testing

### **For Testing**
- ✅ **Automated tests** for all endpoints
- ✅ **Browser testing** of HTTP endpoints
- ✅ **Network simulation** for realistic testing
- ✅ **Easy debugging** with detailed logging

### **For Production**
- ✅ **Zero impact** on release builds
- ✅ **Seamless switching** between fake and real
- ✅ **Backward compatible** with existing code

## 🔄 Integration with DataHandler

The fake API integrates seamlessly with your existing `DataHandler`:
- **Same data**: Uses the same dummy articles
- **Same interface**: Compatible with existing ViewModels
- **API-first**: Falls back to fake API when real API fails
- **Consistent**: Same data structure across all layers

## 🚨 Troubleshooting

### **Common Issues**

#### **Server Won't Start**
```java
// Check if port is in use
Log.d(TAG, "Server running: " + ApiServiceFactory.isFakeApiServerRunning());

// Try different port
boolean started = ApiServiceFactory.startFakeApiServer(context, 8081);
```

#### **BuildConfig Not Found**
```kotlin
// Add to build.gradle.kts
android {
    defaultConfig {
        buildConfigField("boolean", "USE_FAKE_API", "true")
    }
    buildTypes {
        debug { buildConfigField("boolean", "USE_FAKE_API", "true") }
        release { buildConfigField("boolean", "USE_FAKE_API", "false") }
    }
}
```

#### **Emulator Connection Issues**
- **Emulator**: Use `10.0.2.2` (not `localhost` or `127.0.0.1`)
- **Physical device**: Use your computer's IP address
- **Check permissions**: Ensure `INTERNET` permission is granted

#### **Memory Leaks**
```java
// Always stop server in onDestroy()
@Override
protected void onDestroy() {
    super.onDestroy();
    ApiServiceFactory.stopFakeApiServer();
}
```

### **Debug Commands**
```java
// Log current configuration
ApiServiceFactory.logCurrentConfiguration();

// Check server status
Log.d(TAG, "Server running: " + ApiServiceFactory.isFakeApiServerRunning());

// Test API service
ApiTestUtils.testArticleApiService(apiService, callback);
```
