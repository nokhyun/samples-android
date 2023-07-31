package com.nokhyun.third.composable

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nokhyun.third.ThirdViewModel

@Composable
fun ThirdNavHost(
    navController: NavHostController = rememberNavController()
) {
    val thirdViewModel = viewModel<ThirdViewModel>()

    NavHost(navController = navController, startDestination = ThirdRoute.Default.route) {
        composable(ThirdRoute.Default.route) {
            ThirdContent(
                thirdViewModel
            ) {
                navController.navigate(ThirdRoute.Detail.route)
            }
        }

        composable(ThirdRoute.Detail.route) {
            ThirdDetailScreen()
        }
    }
}

internal sealed class ThirdRoute(
    val route: String
) {
    object Default : ThirdRoute(route = "Default")
    object Detail : ThirdRoute(route = "Detail")
}