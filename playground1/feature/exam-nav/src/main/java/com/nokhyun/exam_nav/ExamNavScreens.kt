package com.nokhyun.exam_nav

sealed class ExamNavScreens(
    open val name: String,
    open val route: String
) {
    data class Home(
        override val name: String = "HomeScreen",
        override val route: String = "Home"
    ) : ExamNavScreens(name, route)

    data class Two(
        override val name: String = "TwoScreen",
        override val route: String = "Two"
    ) : ExamNavScreens(name, route)
}