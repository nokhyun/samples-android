package com.nokhyun.uiexam.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.nokhyun.uiexam.logger

@Composable
fun AnnotatedClickableText(){
    val annotatedText = buildAnnotatedString {
        append("Click ")

        pushStringAnnotation(
            tag = "URL", annotation = "https://developer.android.com"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ){
            append("here")
        }

        pop()
    }

    ClickableText(text = annotatedText, onClick = { offset ->
        logger { "offset: $offset" }
        annotatedText.getStringAnnotations(
            tag = "URL", start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            logger { "Clicked URL: ${annotation.item}"}
        }
    })
}