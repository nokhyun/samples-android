plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("nokhyun.android.hilt")
}

android {
    namespace = "com.nokhyun.third"
    compileSdk = 33

    defaultConfig {
        minSdk = com.nokhyun.playground1.Configuration.minSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
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

    buildFeatures {
        buildConfig = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation(project(":domain:passenger"))
    implementation(project(":data:fakePaging"))
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
//    implementation(libs.customview.poolingcontainer)
//    implementation(libs.ui.android)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(libs.paging3)
    implementation(libs.paging3.compose)
    implementation(libs.compose.coil)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiTooling)
    implementation(libs.compose.viewModel)
    implementation(libs.compose.material)
    implementation(libs.compose.navigation)
    implementation(libs.compose.vico)
    implementation(libs.compose.material3)
//    implementation(libs.compose.activity)
//    implementation(libs.compose.foundation)
}