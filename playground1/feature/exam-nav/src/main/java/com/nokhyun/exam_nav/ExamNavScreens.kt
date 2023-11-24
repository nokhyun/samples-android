package com.nokhyun.exam_nav

internal sealed class ExamNavScreens(
    override val name: String,
    override val route: String
) : Screens(name, route) {
    data object Home : ExamNavScreens("HomeScreen", "Home")
    data object Two : ExamNavScreens("TwoScreen", "Two/{userId}")
    data object Profile : ExamNavScreens("ProfileScreen", "Profile?id={id}")

    data object Test : ExamNavScreens("Ex", "ex")
}

internal sealed class IntroScreens(
    override val name: String,
    override val route: String
) : Screens(name, route) {
    data object First : IntroScreens("FirstScreen", "First")
}

internal open class Screens(
    open val name: String,
    open val route: String
)