plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins{
        register("androidHiltPlugin"){
            id = "nokhyun.android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }
    }
}