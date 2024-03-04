plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("nokhyun.android.hilt")
}

android {
    namespace = "com.nokhyun.uiexam"
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":feature:exam-nav"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    implementation(libs.paging3.compose)
    implementation(libs.compose.coil)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiTooling)
    implementation(libs.compose.viewModel)
    implementation(libs.compose.material)
    implementation(libs.compose.navigation)
    implementation(libs.compose.vico)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
}