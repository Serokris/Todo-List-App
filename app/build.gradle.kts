plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.to_dolistapp"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        isEnabled = true
    }
}

val kotlinVersion: String by rootProject.extra
val navigationVersion: String by rootProject.extra
val roomVersion: String by rootProject.extra
val daggerVersion: String by rootProject.extra
val dataStoreVersion: String by rootProject.extra
val jUnitVersion: String by rootProject.extra
val androidTestExtensionVersion: String by rootProject.extra
val lifecycleVersion = "2.4.0"

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    // Testing
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidTestExtensionVersion")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Room components
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")
}