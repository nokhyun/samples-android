package com.nokhyun.uiexam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GraphicExam() {
    Column {
        DrawBehind()
        DrawWithCache()
        GraphicsLayerExam()
    }
}

@Composable
fun DrawBehind() {
    androidx.compose.material.Text(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    Color(0xFFBBAAEE),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .padding(4.dp),
        text = "Hello Compose"
    )
}

@Composable
fun DrawWithCache() {
    Text(
        modifier = Modifier
            .drawWithCache {
                val brush = Brush.linearGradient(
                    listOf(
                        Color(0XFF9E82F0),
                        Color(0XFF42A5F5)
                    )
                )

                onDrawBehind {
                    drawRoundRect(
                        brush,
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                }
            }
            .padding(4.dp),
        text = "Hello Compose"
    )
}

@Composable
fun GraphicsLayerExam() {
    var slideX by remember { mutableFloatStateOf(0.0f) }
    var slideY by remember { mutableFloatStateOf(0.0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = slideX,
            onValueChange = { slideX = it },
            valueRange = 0f..50f,
        )

        Slider(
            value = slideY,
            onValueChange = { slideY = it },
            valueRange = 0f..50f,
        )

        Image(
            modifier = Modifier
                .graphicsLayer {
                    this.scaleX = slideX
                    this.scaleY = slideY
                },
            painter = painterResource(id = com.google.android.material.R.drawable.ic_arrow_back_black_24),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun GraphicExamPreview() {
    GraphicExam()
}
