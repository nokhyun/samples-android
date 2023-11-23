package com.nokhyun.exam_nav

sealed class ExamNavScreens(
    open val name: String,
    open val route: String
) {
    data object Home : ExamNavScreens("HomeScreen", "Home")
    data object Two : ExamNavScreens("TwoScreen", "Two")
    data object Profile : ExamNavScreens("ProfileScreen", "Profile")
}