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

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}