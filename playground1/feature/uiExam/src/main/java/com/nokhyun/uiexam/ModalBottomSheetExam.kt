package com.nokhyun.uiexam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetExam() {
    var showBottomSheet by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier
            .wrapContentSize(),
        onClick = { showBottomSheet = true }
    ) {
        Text(text = "SHOW!!!")
    }

    Box(
    ) {
        val items: List<String> = mutableListOf<String>().apply {
            repeat(6) {
                add("이것은 ${it}번이다")
            }
        }

        BetterModalBottomSheet(
            showBottomSheet,
            onDismissRequest = {
                showBottomSheet = false
            },
            content = {
                LazyColumn(
                ) {
                    items(items) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )

                        Divider(modifier = Modifier.background(Color.Blue))
                    }
                }
            },
            dragHandle = null
        )
    }


//    Scaffold(
//        modifier = Modifier
//    ) {
//        Button(onClick = { showBottomSheet = true }) {
//            Text(text = "SHOW!!!")
//        }
//
//        Box(
//            modifier = Modifier.padding(it)
//        ) {
//            val items: List<String> = mutableListOf<String>().apply {
//                repeat(6) {
//                    add("이것은 ${it}번이다")
//                }
//            }
//
//            BetterModalBottomSheet(
//                showBottomSheet,
//                onDismissRequest = {
//                    showBottomSheet = false
//                },
//                content = {
//                    LazyColumn(
//                    ) {
//                        items(items) {
//                            Text(
//                                modifier = Modifier.fillMaxWidth(),
//                                text = it,
//                                fontSize = 24.sp,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Divider(modifier = Modifier.background(Color.Blue))
//                        }
//                    }
//                },
//            )
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetterModalBottomSheet(
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = WindowInsets.displayCutout,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            scrimColor = scrimColor,
            dragHandle = dragHandle,
            windowInsets = windowInsets
        ) {
            Column(modifier = Modifier.padding(bottom = bottomPadding)) {
                content()
            }
        }
    }
}