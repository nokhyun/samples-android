plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.androidPlugin)
}

gradlePlugin {
    plugins {
        register("androidHiltPlugin"){
            id = "nokhyun.android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }
    }
}