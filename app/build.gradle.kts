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
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // Material 3
    implementation("androidx.compose.material3:material3:1.2.0")
    
    // For backward compatibility with Material 3 components
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    
    // Fragment KTX for viewModels()
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    
    // AndroidX Preference Library
    implementation("androidx.preference:preference-ktx:1.2.1")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}