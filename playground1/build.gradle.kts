// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.ksp) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22" apply false
}