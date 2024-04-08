package com.nokhyun.exam_nav

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
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

//    val modalBottomSheetState = rememberModalBottomSheetState(
//        confirmValueChange = { false }
//    )

//    var isShow by remember { mutableStateOf(false) }

    Column {
//        if (isShow) {
        /* xml bottomSheet */
//            BottomSheetScaffold(
//                sheetContent = {
//                    val numbers = mutableListOf<String>().apply {
//                        repeat(100) {
//                            add(it.toString())
//                        }
//                    }
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.White)
//                    ) {
//                        items(numbers.size) {
//                            Text(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(12.dp),
//                                text = numbers[it],
//                                fontSize = 28.sp,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }) {
//                Box(
//                    modifier = Modifier.padding(it)
//                ){
//                    Text(text = "asd")
//                }
//            }

//            ModalBottomSheetLayout(
//                sheetGesturesEnabled = false,
//                sheetContent = {
//                    val numbers = mutableListOf<String>().apply {
//                        repeat(100) {
//                            add(it.toString())
//                        }
//                    }
//
//                    CenterAlignedTopAppBar(
//                        title = {
//                            Text("ExamNavHost 테스트")
//                        },
//                        actions = {
//                            IconButton(onClick = {
//                                scope.launch { state.hide() }
//                            }) {
//                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
//                            }
//                        },
//                        colors = TopAppBarDefaults.topAppBarColors(
//                            containerColor = Color.White
//                        )
//                    )
//
//                    Spacer(
//                        modifier = Modifier
//                            .padding(start = 4.dp, end = 4.dp)
//                            .fillMaxWidth()
//                            .height(1.dp)
//                            .background(Color.Black)
//                    )
//
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .fillMaxHeight(0.4f)
//                            .background(Color.White)
//                    ) {
//                        items(numbers.size) {
//                            Text(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(12.dp),
//                                text = numbers[it],
//                                fontSize = 28.sp,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                },
//                sheetState = state,
//                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//            ) {
//                Button(onClick = {
//                    scope.launch {
//                        state.show()
//                    }
//                }) {
//                    Text(text = "Show!")
//                }
//            }

        // material3
//            ModalBottomSheet(
//                modifier = Modifier.fillMaxHeight(0.4f),
////                properties = ModalBottomSheetProperties(
////                    isFocusable = false,
////                    shouldDismissOnBackPress = false,
////                    securePolicy = SecureFlagPolicy.Inherit
////                ),
//                sheetState = modalBottomSheetState,
////                tonalElevation = 24.dp,
////                scrimColor = Color.Red,
//                onDismissRequest = {
//                    isShow = false
//                },
//                content = {
//                    val numbers = mutableListOf<String>().apply {
//                        repeat(100) {
//                            add(it.toString())
//                        }
//                    }
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.White)
//                    ) {
//                        items(numbers.size) {
//                            Text(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(12.dp),
//                                text = numbers[it],
//                                fontSize = 28.sp,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                },
//                dragHandle = null
//            )
//        }
//
//        Button(onClick = { isShow = true }) {
//            Text(text = "Show!")
//        }

        NavHost(
            navController = navHostController,
            startDestination = IntroScreens.First.route
        ) {
            composable(IntroScreens.First.route) {
                Button(onClick = { navHostController.navigate("Home") }) {
                    Text(text = "Hello World!")
//                    deepLinkPendingIntent?.send()
                }
            }

            mainGraph(navHostController)
        }
    }
}

internal fun NavGraphBuilder.mainGraph(navHostController: NavHostController) {
    composable(ExamNavScreens.Home.route) { backStackEntry ->
        HomeScreen(
            onTwoClick = {
                navHostController.navigate("Two/UserId1234") {
//                    popUpTo(navHostController.graph.findStartDestination().id)
                    navHostController.navigate("Profile?id=123")
                }
            },
            onProfileClick = {
                navHostController.navigate("Profile?id=123")
            },
            navHostController
        )
    }

    composable(
        route = ExamNavScreens.Two.route,
        arguments = listOf(navArgument("userId") { type = NavType.StringType })
    ) { backStackEntry ->
        val userViewModel: UserViewModel = viewModel()

        TwoScreen(
            navHostController,
            backStackEntry.arguments?.getString("userId") ?: "empty",
            userViewModel
        )
    }

    composable(
        route = ExamNavScreens.Profile.route,
        deepLinks = listOf(navDeepLink { uriPattern = "https://www.example.com/{id}" })
    ) { backStackEntry ->
        ProfileScreen(navHostController, backStackEntry.arguments?.getString("id") ?: "emptyId")
    }
}