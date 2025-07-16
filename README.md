# KMM Payments App

A Kotlin Multiplatform Mobile (KMM) app for sending payments and viewing transaction history. Shared business logic for Android and iOS.

## Setup

### Prerequisites
- Android Studio Giraffe or later
- Kotlin Multiplatform plugin
- JDK 17+
- Android SDK 34+
- Firebase project with Firestore enabled

### Clone and Build
```bash
git clone https://github.com/ihsanulhaq89/kmm-payments-app.git
cd kmm-test
./gradlew clean build
```

### Architecture
- shared: Business logic (Ktor, Firestore, models, repositories, services, use cases) 
- androidApp: Jetpack Compose UI
- iOS module (future-ready): Can use shared logic via Kotlin Native and SwiftUI

### Running the App
- Open in Android Studio
- Select androidApp
- Run on emulator or physical device