export interface ProjectFile {
  path: string;
  name: string;
  category: 'config' | 'workflow' | 'core' | 'domain' | 'data' | 'feature' | 'app' | 'docs';
  language: 'kotlin' | 'groovy' | 'yaml' | 'markdown' | 'xml' | 'properties' | 'toml';
  content: string;
}

export const ANDROID_PROJECT_FILES: ProjectFile[] = [
  {
    path: 'settings.gradle.kts',
    name: 'settings.gradle.kts',
    category: 'config',
    language: 'kotlin',
    content: `pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\\\.android.*")
                includeGroupByRegex("com\\\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NexaIsland"

// Modular Architecture
include(":app")
include(":core:common")
include(":core:design")
include(":core:datastore")
include(":core:permissions")
include(":core:overlay")
include(":domain")
include(":data")
include(":feature:dashboard")
include(":feature:island")`
  },
  {
    path: 'build.gradle.kts',
    name: 'build.gradle.kts',
    category: 'config',
    language: 'kotlin',
    content: `// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}`
  },
  {
    path: 'gradle.properties',
    name: 'gradle.properties',
    category: 'config',
    language: 'properties',
    content: `org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
android.defaults.buildfeatures.buildconfig=true
android.nonTransitiveRClass=true`
  },
  {
    path: 'gradle/libs.versions.toml',
    name: 'libs.versions.toml',
    category: 'config',
    language: 'toml',
    content: `[versions]
agp = "8.3.2"
kotlin = "1.9.23"
coreKtx = "1.12.0"
lifecycleRuntimeKtx = "2.7.0"
activityCompose = "1.8.2"
composeBom = "2024.04.00"
composeCompiler = "1.5.11"
hilt = "2.51.1"
ksp = "1.9.23-1.0.20"
coroutines = "1.8.0"
datastore = "1.0.0"
material3 = "1.2.1"
appcompat = "1.6.1"
material = "1.11.0"
navigationCompose = "2.7.7"
coil = "2.6.0"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "material3" }
androidx-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }`
  },
  {
    path: '.github/workflows/android-build.yml',
    name: 'android-build.yml',
    category: 'workflow',
    language: 'yaml',
    content: `name: Android Build (Debug)

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]
  workflow_dispatch:

concurrency:
  group: build-\${{ github.ref }}
  cancel-in-progress: true

jobs:
  build-debug:
    name: Assemble Debug APK
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4
        with:
          fetch-depth: 1

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Gradle Cache
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: \${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties', 'gradle/libs.versions.toml') }}
          restore-keys: |
            \${{ runner.os }}-gradle-

      - name: Make Gradle Wrapper Executable
        run: chmod +x ./gradlew

      - name: Build Debug APK
        run: ./gradlew assembleDebug --stacktrace --no-daemon

      - name: Verify Debug APK
        run: |
          echo "Listing generated APKs..."
          ls -la app/build/outputs/apk/debug/

      - name: Upload Debug APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: NexaIsland-Debug
          path: app/build/outputs/apk/debug/*.apk
          retention-days: 14`
  },
  {
    path: '.github/workflows/android-release.yml',
    name: 'android-release.yml',
    category: 'workflow',
    language: 'yaml',
    content: `name: Android Build (Release Signed)

on:
  workflow_dispatch:
  push:
    tags:
      - 'v*'

jobs:
  build-release:
    name: Assemble Signed Release APK
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4
        with:
          fetch-depth: 1

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Gradle Cache
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: \${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties', 'gradle/libs.versions.toml') }}
          restore-keys: |
            \${{ runner.os }}-gradle-

      - name: Validate GitHub Secrets
        run: |
          if [ -z "\${{ secrets.ANDROID_KEYSTORE_BASE64 }}" ]; then
            echo "::error::ANDROID_KEYSTORE_BASE64 is missing! Please configure repository secret."
            exit 1
          fi
          if [ -z "\${{ secrets.ANDROID_KEYSTORE_PASSWORD }}" ]; then
            echo "::error::ANDROID_KEYSTORE_PASSWORD is missing! Please configure repository secret."
            exit 1
          fi
          if [ -z "\${{ secrets.ANDROID_KEY_ALIAS }}" ]; then
            echo "::error::ANDROID_KEY_ALIAS is missing! Please configure repository secret."
            exit 1
          fi
          if [ -z "\${{ secrets.ANDROID_KEY_PASSWORD }}" ]; then
            echo "::error::ANDROID_KEY_PASSWORD is missing! Please configure repository secret."
            exit 1
          fi
          echo "All required release signing secrets are present."

      - name: Decode Android Keystore
        run: |
          mkdir -p app/keystore
          echo "\${{ secrets.ANDROID_KEYSTORE_BASE64 }}" | base64 --decode > app/keystore/release.jks
          if [ ! -s app/keystore/release.jks ]; then
            echo "::error::Decoded keystore file is empty or corrupted."
            exit 1
          fi
          echo "Keystore successfully decoded to app/keystore/release.jks"

      - name: Make Gradle Wrapper Executable
        run: chmod +x ./gradlew

      - name: Build Signed Release APK
        env:
          KEYSTORE_FILE: "keystore/release.jks"
          KEYSTORE_PASSWORD: \${{ secrets.ANDROID_KEYSTORE_PASSWORD }}
          KEY_ALIAS: \${{ secrets.ANDROID_KEY_ALIAS }}
          KEY_PASSWORD: \${{ secrets.ANDROID_KEY_PASSWORD }}
        run: ./gradlew assembleRelease --stacktrace --no-daemon

      - name: Clean Keystore File
        if: always()
        run: |
          rm -f app/keystore/release.jks
          echo "Cleaned temporary keystore file."

      - name: Verify Release APK
        run: |
          echo "Listing generated Release APKs..."
          ls -la app/build/outputs/apk/release/

      - name: Upload Release APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: NexaIsland-Release
          path: app/build/outputs/apk/release/*.apk
          retention-days: 30`
  },
  {
    path: 'app/build.gradle.kts',
    name: 'app/build.gradle.kts',
    category: 'app',
    language: 'kotlin',
    content: `plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.nexa.island"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nexa.island"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystoreFile = System.getenv("KEYSTORE_FILE") ?: "keystore/release.jks"
            val file = file(keystoreFile)
            if (file.exists()) {
                storeFile = file
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("KEY_ALIAS") ?: ""
                keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:datastore"))
    implementation(project(":core:permissions"))
    implementation(project(":core:overlay"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:island"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}`
  },
  {
    path: 'app/src/main/AndroidManifest.xml',
    name: 'AndroidManifest.xml',
    category: 'app',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" tools:ignore="ProtectedPermissions" />

    <application
        android:name=".NexaApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.NexaIsland">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:launchMode="singleTop">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".service.IslandOverlayService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="specialUse" />

        <service
            android:name="com.nexa.island.data.collector.NotificationCollectorService"
            android:exported="true"
            android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
            <intent-filter>
                <action android:name="android.service.notification.NotificationListenerService" />
            </intent-filter>
        </service>
    </application>
</manifest>`
  },
  {
    path: 'domain/src/main/java/com/nexa/island/domain/model/IslandEvent.kt',
    name: 'IslandEvent.kt',
    category: 'domain',
    language: 'kotlin',
    content: `package com.nexa.island.domain.model

enum class EventPriority(val level: Int) {
    INCOMING_CALL(100),
    NAVIGATION(90),
    NOTIFICATION(80),
    MUSIC(60),
    BATTERY_ALERT(50),
    SYSTEM_STATUS(20),
    IDLE(0)
}

sealed interface IslandEvent {
    val id: String
    val priority: EventPriority
    val timestamp: Long

    data class IncomingCall(
        override val id: String,
        val callerName: String,
        val callerNumber: String,
        override val priority: EventPriority = EventPriority.INCOMING_CALL,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Notification(
        override val id: String,
        val packageName: String,
        val appName: String,
        val title: String,
        val message: String,
        override val priority: EventPriority = EventPriority.NOTIFICATION,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Music(
        override val id: String,
        val trackTitle: String,
        val artist: String,
        val isPlaying: Boolean,
        val durationMs: Long,
        val positionMs: Long,
        val packageName: String,
        override val priority: EventPriority = EventPriority.MUSIC,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class BatteryAlert(
        override val id: String,
        val percentage: Int,
        val isCharging: Boolean,
        val temperatureCelsius: Float? = null,
        val isLowBattery: Boolean = false,
        override val priority: EventPriority = EventPriority.BATTERY_ALERT,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent
}`
  },
  {
    path: 'core/design/src/main/java/com/nexa/island/core/design/theme/AnimationSpecs.kt',
    name: 'AnimationSpecs.kt',
    category: 'core',
    language: 'kotlin',
    content: `package com.nexa.island.core.design.theme

import androidx.compose.animation.core.spring

object IslandAnimationSpecs {
    const val SPRING_DAMPING_RATIO = 0.82f
    const val SPRING_STIFFNESS = 350f

    fun <T> islandSpring() = spring<T>(
        dampingRatio = SPRING_DAMPING_RATIO,
        stiffness = SPRING_STIFFNESS
    )
}`
  },
  {
    path: 'core/overlay/src/main/java/com/nexa/island/core/overlay/OverlayManager.kt',
    name: 'OverlayManager.kt',
    category: 'core',
    language: 'kotlin',
    content: `package com.nexa.island.core.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var activeView: View? = null
    private var isAttached: Boolean = false

    fun createLayoutParams(widthPx: Int, heightPx: Int, offsetYPx: Int): WindowManager.LayoutParams {
        val flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

        return WindowManager.LayoutParams(
            widthPx,
            heightPx,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            flags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = offsetYPx
        }
    }
}`
  },
  {
    path: 'README.md',
    name: 'README.md',
    category: 'docs',
    language: 'markdown',
    content: `# Nexa Island — Dynamic Island untuk Android & MIUI 13\n\nPanduan lengkap dari nol hingga menghasilkan APK Release via GitHub Actions...`
  }
];
