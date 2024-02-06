package com.nokhyun.uiexam.swipeableExam

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class DragValue { Start, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableScreen() {
    val density = LocalDensity.current

    val anchors = DraggableAnchors<DragValue> {
        DragValue.Start at 0.dp.value
        DragValue.End at 132.dp.value
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,                                         // 드래그의 시작점
            positionalThreshold = { totalDistance: Float -> totalDistance * 5f },   // 앵커 사이의 거리에 따라 콘텐츠가 다음 앵커로 애니메이션을 적용할지 결정하는데 사용하는 람다
            velocityThreshold = { with(density) { 100.dp.toPx() } },                // 애니메이션 속도를 리턴하는 람다
            anchors = anchors,
            animationSpec = tween(),
            confirmValueChange = {                                       // 변경사항을 거부하는 데 사용할 수 있는 람다
//                it == DragValue.Start
                true
            }
        )
    }

    Box(
        modifier = Modifier
            .width(96.dp)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal)
                .background(Color.Cyan)
        )
    }
}