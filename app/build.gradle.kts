plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.rafdi.vitechasia.blog"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rafdi.vitechasia.blog"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Glide for image loading
    implementation(libs.glide)

    // Material 3
    implementation(libs.material3)

    // For backward compatibility with Material 3 components
    implementation(libs.material3.window.size)

    // Fragment KTX for viewModels()
    implementation(libs.fragment.ktx)

    // AndroidX Preference Library
    implementation(libs.preference.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    
    // Gson for JSON serialization/deserialization
    implementation(libs.gson)
    
    // Retrofit for network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    
    // Coroutines for asynchronous programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    
    // Room for local database
    implementation("androidx.room:room-runtime:2.4.3")
    annotationProcessor("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}