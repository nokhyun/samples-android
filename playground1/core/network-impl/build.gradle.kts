plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("nokhyun.android.hilt")
}

android {
    namespace = "com.nokhyun.network_impl"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:network-api"))
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serializationAdapater)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.serializationConverter)
}