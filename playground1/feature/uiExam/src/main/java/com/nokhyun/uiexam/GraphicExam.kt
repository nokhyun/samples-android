package com.nokhyun.uiexam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class GraphicSliderState(
    private val initX: Float,
    private val initY: Float
) {
    var x by mutableFloatStateOf(initX)
    var y by mutableFloatStateOf(initY)

    companion object {
        val Saver: Saver<GraphicSliderState, *> = listSaver(
            save = {
                logger { "save: $it" }
                listOf(it.x, it.y)
            },
            restore = {
                GraphicSliderState(initX = it[0], initY = it[1])
            }
        )
    }
}

@Composable
fun rememberSliderState(initX: Float = 3f, initY: Float = 3f) = rememberSaveable(initX, initY, saver = GraphicSliderState.Saver) { GraphicSliderState(initX, initY) }

@Composable
fun GraphicExam() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        DrawBehind()
        DrawWithCache()
        GraphicsLayerExam()
        DrawWithContentExam()
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
    val sliderState = rememberSliderState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = sliderState.x,
            onValueChange = { sliderState.x = it },
            valueRange = 0f..50f,
        )

        Slider(
            value = sliderState.y,
            onValueChange = { sliderState.y = it },
            valueRange = 0f..50f,
        )

        Image(
            modifier = Modifier
                .graphicsLayer {
                    this.scaleX = sliderState.x
                    this.scaleY = sliderState.y
                },
            painter = painterResource(id = com.google.android.material.R.drawable.ic_arrow_back_black_24),
            contentDescription = null
        )
    }
}

@Composable
fun DrawWithContentExam() {
    var pointerOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput("dragging") {
                detectDragGestures { change, dragAmount ->
                    pointerOffset += dragAmount
                }
            }
            .onSizeChanged {
                pointerOffset = Offset(it.width / 2f, it.height / 2f)
            }
            .drawWithContent {
                drawContent()

                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Transparent, Color.Black),
                        center = pointerOffset,
                        radius = 100.dp.toPx()
                    )
                )
            }
    ) {
        Text(
            modifier = Modifier.clickable {
                pointerOffset = Offset(0f, 0f)
            },
            text = "Hello Compose",
            fontSize = 32.sp
        )
    }
}

@Preview
@Composable
fun GraphicExamPreview() {
    GraphicExam()
}
