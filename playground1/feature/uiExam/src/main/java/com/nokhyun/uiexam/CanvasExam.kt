package com.nokhyun.uiexam

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun CanvasExam() {
    CanvasSample()
    CanvasCircle()
}

@Composable
private fun CanvasSample() {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val width = size.width
        val height = size.height

        drawLine(
            start = Offset(x = width, y = 0f),
            end = Offset(x = 0f, y = height),
            color = Color.Blue
        )
    }
}

@Composable
private fun CanvasCircle() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
//        scale(scaleX = 10f, scaleY = 15f){
//            defaultCircle()
//        }

//        translate(left = 100f, top = -300f) {
//            defaultCircle()
//        }

//        rotate(degrees = 45f) {
//            defaultRect()
//        }

        defaultInset()
    }
}

private fun DrawScope.defaultCircle() {
    drawCircle(Color.Blue, radius = 200.dp.toPx())
}

private fun DrawScope.defaultRect() {
    drawRect(
        color = Color.Gray,
        topLeft = Offset(x = size.width / 3f, y = size.height / 3f),
        size = size / 3f
    )
}

private fun DrawScope.defaultInset() {
    val canvasQuadrantSize = size / 2f
    inset(horizontal = 50f, vertical = 30f) {
        drawRect(color = Color.Green, size = canvasQuadrantSize)
    }
}