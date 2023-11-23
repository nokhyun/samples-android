package com.nokhyun.exam_nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun ExamNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = ExamNavScreens.Home().name
    ) {
        // TODO 데이터 전달 레퍼런스 볼 것
        composable("HomeScreen"){
            // TODO
            HomeScreen()
        }

        composable("TwoScreen") {
            // TODO
            TwoScreen()
        }
    }
}