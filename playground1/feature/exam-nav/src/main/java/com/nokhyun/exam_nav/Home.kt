package com.nokhyun.exam_nav

import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTwoClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val state = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false,
        confirmValueChange = { false } // outSideTouch disable
    )
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current

    onBackPressedDispatcher?.addCallback(owner = lifecycleOwner) {
        scope.launch {
            if (state.targetValue == ModalBottomSheetValue.Expanded) {
                state.hide()
            } else {
//                onFinish()
                navController.navigateUp()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetGesturesEnabled = false,
        sheetContent = {
            val numbers = mutableListOf<String>().apply {
                repeat(100) {
                    add(it.toString())
                }
            }

            CenterAlignedTopAppBar(
                title = {
                    Text("ExamNavHost 테스트")
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { state.hide() }
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            Spacer(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .background(Color.White)
            ) {
                items(numbers.size) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        text = numbers[it],
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        sheetState = state,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Button(onClick = {
                scope.launch {
                    state.show()
                }
            }) {
                Text(text = "Show!")
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 4.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = onTwoClick
            ) {
                Text(text = "NavigateToTwo")
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 4.dp),
                onClick = onProfileClick
            ) {
                Text(text = "NavigateToDeepLink")
            }
        }
    }
}