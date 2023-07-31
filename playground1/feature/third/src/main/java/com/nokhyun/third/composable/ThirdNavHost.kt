package com.nokhyun.third.composable

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
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
                logger {
                    "value: ${thirdViewModel.atomicInteger.get()}"
                }
                navController.navigate(ThirdRoute.Detail.route)
            }
        }

        singleComposable(ThirdRoute.Detail.route, navHostController = navController, onEvent = {
            thirdViewModel.atomicInteger.getAndIncrement()
        }) { navBackStackEntry ->
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

internal fun NavGraphBuilder.singleComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    navHostController: NavHostController,
    onEvent: () -> Unit,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route, arguments, deepLinks) { navBackStackEntry ->
        if (navHostController.currentDestination?.route!! != navBackStackEntry.destination.route) {
            onEvent()
        }

        content(navBackStackEntry)
    }
}