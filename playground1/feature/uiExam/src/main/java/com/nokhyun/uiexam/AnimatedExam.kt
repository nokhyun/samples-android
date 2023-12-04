package com.nokhyun.uiexam

import android.view.Window
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedExam() {
    Column {
        AlphaExam()
        ColorExam()
    }
}

@Composable
fun AlphaExam() {
    var visible by remember { mutableStateOf(true) }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(200.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                }
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Green)
        )
        Button(
            modifier = Modifier
                /*
                * 해당 modifier 제거 시 버튼 상단에 외부 패딩 생김.
                * windowInsetsTopHeight 사용하여 상단 값 재 정의
                * */
                .windowInsetsTopHeight(WindowInsets(top = ButtonDefaults.MinHeight))
                .weight(1f),
            onClick = { visible = false },
        ) {
            Text(
                text = "alpha"
            )
        }
    }
}

@Composable
fun ColorExam() {
    var animateBackgroundColor by remember { mutableStateOf(true) }
    val animateColor by animateColorAsState(
        targetValue = if (animateBackgroundColor) Color.Cyan else Color.Blue,
        label = "color"
    )
    var test by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(200.dp)
        .clip(RoundedCornerShape(8.dp))
        .onSizeChanged {
            test = it.height.dp
        }
        .drawBehind {
            drawRect(animateColor)
        })

    Button(
        modifier = Modifier.offset(0.dp, (-6).dp),
        onClick = {
            animateBackgroundColor = !animateBackgroundColor
        }) {
        Text(text = "Change Color")
    }
}

@Preview
@Composable
fun AnimatedExamPreview() {
    AnimatedExam()
}