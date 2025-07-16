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

### Running all tests
```bash
 ./gradlew :shared:testDebugUnitTest --rerun-tasks
```

OR
```bash
Run > Edit Configurations > + > Android Tests
Select Tests in 'My_Application.shared.commonTest'
Then -> Apply > Run
````
### KMM Architecture and Cross-Platform Potential
The shared module contains API clients, database interactions, and business logic written in Kotlin. Android uses Jetpack Compose for UI. iOS can reuse the shared logic via Kotlin Native, enabling faster development and feature parity across platforms.