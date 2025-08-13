// Top-level build file (e.g., /build.gradle.kts)
// where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false // Add this line if you want to make kapt available to all modules
    // without applying it directly here.
    // It's often applied directly in the app module's plugins block.
}
