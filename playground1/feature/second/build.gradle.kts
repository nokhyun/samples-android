import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.nokhyun.playground1.Configuration

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val properties = gradleLocalProperties(rootDir)

android {
    namespace = "com.nokhyun.second"
    compileSdk = 33

    defaultConfig {
        minSdk = Configuration.minSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "tossClientKey", properties.getProperty("tossClientKey"))
            buildConfigField("String", "tossSecretKey", properties.getProperty("tossSecretKey"))
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "tossClientKey", properties.getProperty("tossClientKey"))
            buildConfigField("String", "tossSecretKey", properties.getProperty("tossSecretKey"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}