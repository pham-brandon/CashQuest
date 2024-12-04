plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Firebase services plugin
}

android {
    namespace = "com.personal_finance_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.personal_finance_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

    buildToolsVersion = "34.0.0"
    ndkVersion = "26.1.10909125"
}

dependencies {
    // AndroidX and Material Design dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)

    // Play Services
    implementation(libs.play.services.mlkit.text.recognition)
    implementation("com.google.android.gms:play-services-base:18.1.0") // Base Play Services

    // Firebase dependencies (using BOM for consistent versions)
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database:20.1.0")
    implementation("com.google.firebase:firebase-auth:21.3.0")
    implementation("com.google.firebase:firebase-analytics:21.2.2")

    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Gson for JSON handling
    implementation("com.google.code.gson:gson:2.8.9")

    // ML Kit
    implementation("com.google.mlkit:text-recognition:16.0.1")
}

// Apply the Google Services plugin for Firebase
apply(plugin = "com.google.gms.google-services")