plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.apptaza"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.apptaza"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")

    // ViewModel / DataStore
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Lifecycle runtime (for collecting flows in lifecycleScope)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // LiveData (if you use it)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")

    // For phone number authentication (SMS)
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")


    // For Google sign-in integration
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    // Retrofit + Gson
    implementation(platform("com.squareup.retrofit2:retrofit-bom:3.0.0"))
    implementation("com.squareup.retrofit2:retrofit")
    implementation("com.squareup.retrofit2:converter-gson")
    implementation("com.google.code.gson:gson:2.13.2")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.2.1"))
    implementation("com.squareup.okhttp3:okhttp") {
        exclude(group = "org.jspecify", module = "jspecify")
    }
}