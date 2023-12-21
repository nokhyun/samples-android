pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
rootProject.name = "playground1"
include(":app")
include(":feature")
include(":feature:first")
include(":core")
include(":core:network-api")
include(":feature:second")
include(":feature:third")
include(":data:fakePaging")
include(":domain:passenger")
include(":feature:uiExam")
include(":core:network-impl")
include(":feature:exam-nav")
include(":feature:startup-api")
