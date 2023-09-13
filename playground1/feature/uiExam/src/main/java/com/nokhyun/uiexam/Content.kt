package com.nokhyun.uiexam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nokhyun.uiexam.text.AnnotatedClickableText
import com.nokhyun.uiexam.text.DifferentFonts

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
    }
}