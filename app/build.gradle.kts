// In app/build.gradle.kts

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)         // <<< ADD THIS LINE to apply the kapt plugin
}

android {
    namespace = "com.example.assigmentapp"
    compileSdk = 36 // Make sure this value (36) is appropriate.
    // The latest stable SDK is usually recommended.
    // For example, if Android 14 is the latest, compileSdk = 34.
    // Android 36 is likely a preview version if it exists when you're reading this.
    // For stability, target the latest public release unless you specifically need preview features.

    defaultConfig {
        applicationId = "com.example.assigmentapp"
        minSdk = 24
        targetSdk = 36 // Should generally match compileSdk, or be the latest stable API level.
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Consider enabling this for release builds (isMinifyEnabled = true)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // For Java 11
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // If you were using Java 1.8 (as in some previous examples):
        // sourceCompatibility = JavaVersion.VERSION_1_8
        // targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "11" // Should match compileOptions Java version
        // If you were using Java 1.8:
        // jvmTarget = "1.8"
    }
    // It's good practice to add viewBinding or dataBinding if you plan to use them
    // buildFeatures {
    //     viewBinding = true
    // }
}

dependencies {
    // AndroidX - Core & UI
    implementation(libs.androidx.core.ktx) // You have this twice, removing one
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material) // Corrected alias based on typical TOML setup
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)  // <<< ADDED for displaying fetched images
    implementation(libs.androidx.core.splashscreen)
    // Networking - Retrofit, OkHttp
    implementation(libs.retrofit.core)            // <<< ADDED
    implementation(libs.retrofit.converter.gson)  // <<< ADDED
    implementation(libs.okhttp.logging.interceptor) // <<< ADDED

    // Image Loading - Glide
    implementation(libs.glide.core)               // <<< ADDED
    kapt(libs.glide.compiler)                     // <<< ADDED for Glide's annotation processor

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
