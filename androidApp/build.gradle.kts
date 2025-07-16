plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.app.myapplication.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.app.myapplication.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets {
        named("androidTest") {
            java.srcDirs("src/androidTest/java")
            resources.srcDirs("src/androidTest/resources")
        }
    }
}

dependencies {
    implementation(projects.shared)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)

    // Koin for Android
    implementation(libs.koin.android)

    // Firebase (Android native SDKs)
    implementation(platform(libs.firebase.bom))

    // Firestore
    implementation(libs.firebase.firestore.ktx)


    androidTestImplementation(libs.cucumber.java)
    androidTestImplementation(libs.cucumber.junit)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.kotest.assertions.core)

    androidTestImplementation(libs.cucumber.junit)
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
}

apply(plugin = "com.google.gms.google-services")