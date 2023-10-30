package com.nokhyun.uiexam

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nokhyun.uiexam.text.AnnotatedClickableText
import com.nokhyun.uiexam.text.DifferentFonts
import kotlinx.coroutines.launch

@Composable
fun UiExamContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .wrapContentSize()    // child size 에 맞춰짐.
            .background(Color.Gray)
    ) {
        Row {
            Switch(checked = true, onCheckedChange = {
            }, thumbContent = {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            })

            AnnotatedClickableText()
        }

        DifferentFonts()

        HorizontalViewPager()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalViewPager() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        10
    }
    val coroutineScope = rememberCoroutineScope()
    val pageCount = 10

    Box {
        LaunchedEffect(key1 = pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                logger { "page Change: $page" }
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
//                    pagerState.scrollToPage(5)
                    pagerState.animateScrollToPage(5)
                }
            },
        ) {
            Text(text = "Jump to Page 5")
        }

        HorizontalPager(
            modifier = Modifier
                .background(Color.LightGray),
            state = pagerState,
            pageSpacing = 0.dp,
            userScrollEnabled = true,
            reverseLayout = false,
            beyondBoundsPageCount = 0,
            key = null,
            pageContent = { page ->
                Text(
                    text = "Page: $page",
                    modifier = Modifier
                        .fillMaxSize()
                        .height(100.dp)
                )
            }
        )

        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxSize()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.Blue
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(20.dp)
                )
            }
        }
    }
}