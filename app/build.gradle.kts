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
        buildConfigField("boolean", "USE_FAKE_API", "true")
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "USE_FAKE_API", "true")
        }
        release {
            buildConfigField("boolean", "USE_FAKE_API", "false")
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
    // AndroidX Core
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Material Design
    implementation(libs.material)
    
    // Glide for image loading
    implementation(libs.glide)
    annotationProcessor("com.github.bumptech.glide:compiler:${libs.versions.glide.get()}")

    // Material 3
    implementation(libs.material3)
    implementation(libs.material3.window.size)
    // AndroidX KTX
    implementation(libs.fragment.ktx)
    implementation(libs.preference.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    
    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    
    // Fake API Server (for testing)
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    
    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    
    // Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}