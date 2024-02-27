package com.nokhyun.uiexam.CanvasExam

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp

@Composable
fun CanvasExamScreen() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = canvasWidth, y = 0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.Black
        )

        rotate(degrees = 45f) {
            translate(left = 100f, top = 10f) {
                scale(scaleX = 1.2f, scaleY = 1.4f) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(x = size.width / 3f, y = size.height / 3f),
                        size = size / 3f
                    )
                }
            }
        }

        inset(horizontal = 50f, vertical = 30f) {
            drawRect(
                color = Color.Gray,
                size = size / 2f
            )
        }
    }
}