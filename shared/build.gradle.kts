plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
}

configurations.all {
    resolutionStrategy {
        force(
            "org.jetbrains.kotlinx:atomicfu:0.23.1",
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1"
        )
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    jvm()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    kotlin {
        sourceSets {
            val commonMain by getting {
                dependencies {
                    // KMP-safe dependencies
                    implementation(libs.ktor.client.core)
                    implementation(libs.ktor.client.json)
                    implementation(libs.ktor.client.serialization)
                    implementation(libs.ktor.client.content.negotiation)
                    implementation(libs.ktor.serialization.kotlinx.json)
                    implementation(libs.kotlinx.serialization.json)
                    implementation(libs.koin.core)
                    implementation(libs.firebase.firestore.kmp)
                    implementation(libs.firebase.auth.kmp)
                    implementation(libs.kotlinx.datetime)
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(libs.kotest.core)
                    implementation(libs.kotest.assertions.core)
                    implementation(libs.cucumber.java)
                    implementation(libs.cucumber.junit)
                    implementation(libs.kotlinx.coroutines.test)
                    implementation(libs.kotest.framework.engine)
                }
            }
            val androidMain by getting {

                dependencies {
                    implementation(libs.koin.android)
                    implementation(libs.ktor.client.okhttp)
                    implementation(libs.firebase.firestore.ktx) // Android-specific Firestore
                }
            }
            val androidUnitTest by getting {
                dependencies {
                    implementation(libs.kotest.runner.junit5)
                    implementation(libs.cucumber.java)
                    implementation(libs.cucumber.junit)
                    implementation(libs.kotest.runner.junit5)
                    implementation(libs.kotest.assertions.core)
                }
            }
            val androidInstrumentedTest by getting {
                dependencies {
                    implementation(libs.kotest.runner.junit5)
                    implementation(libs.cucumber.java)
                    implementation(libs.cucumber.junit)
                    implementation(libs.kotest.assertions.core)
                }
            }
        }
    }

}
tasks.withType<Test> {
    useJUnitPlatform() // For Kotest with JUnit5
    testLogging {
        events("passed", "failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}
android {
    namespace = "com.app.myapplication"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packaging {
        resources {
            excludes += listOf(
                "**/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/*.kotlin_module",
                "META-INF/LICENSE-notice.md" // Explicitly add this
            )
        }
    }
}
