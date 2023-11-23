package com.nokhyun.exam_nav

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun ExamNavHost(
    navHostController: NavHostController
) {
    val id = "exampleId"
    val context = LocalContext.current
    val deepLinkIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.example.com/$id".toUri(),
        context,
        ExamNavActivity::class.java
    )

    val deepLinkPendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(deepLinkIntent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    NavHost(
        navController = navHostController,
        startDestination = ExamNavScreens.Home.route
    ) {
        // TODO 데이터 전달 레퍼런스 볼 것
        composable("Home") { backStackEntry ->
            HomeScreen {
//                navHostController.navigate("Two/UserId1234")
                deepLinkPendingIntent?.send()
            }
        }

        composable(
            route = "Two/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userViewModel: UserViewModel = viewModel()

            TwoScreen(navHostController, backStackEntry.arguments?.getString("userId") ?: "empty", userViewModel)
        }

        composable(
            route = "Profile?id={id}",
            deepLinks = listOf(navDeepLink { uriPattern = "https://www.example.com/{id}" })
        ) { backStackEntry ->
            ProfileScreen(navHostController, backStackEntry.arguments?.getString("id") ?: "emptyId")
        }
    }
}