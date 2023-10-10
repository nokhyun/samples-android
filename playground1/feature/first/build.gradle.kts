import com.nokhyun.playground1.Configuration

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.nokhyun.first"
    compileSdk = 33

    defaultConfig {
        minSdk = Configuration.minSdk
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.fragment)
    implementation(libs.toss.core)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiTooling)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.compose.viewModel)
    implementation(project(mapOf("path" to ":core:network")))
    implementation(project(mapOf("path" to ":core:network-impl")))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val nav_version = "2.6.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation(libs.hilt)
    kapt(libs.hilt.kapt)

    implementation("com.github.nokhyun:NokhyunPermission:1.0.0")
}