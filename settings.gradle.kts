enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("com.google.gms.google-services") version "4.4.3"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repository.apache.org/content/groups/public/") } // Apache JMeter repo
        maven { url = uri("https://jcenter.bintray.com") } // fallback
        maven { url = uri("https://repo1.maven.org/maven2/") } // ensure Maven Central

    }
}

rootProject.name = "My_Application"
include(":androidApp")
include(":shared")
