package com.nokhyun.uiexam.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.nokhyun.uiexam.R
import com.nokhyun.uiexam.logger

@Composable
fun AnnotatedClickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click ")

        pushStringAnnotation(
            tag = "URL", annotation = "https://developer.android.com"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ) {
            append("here")
        }

        pop()
    }

    ClickableText(text = annotatedText, onClick = { offset ->
        logger { "offset: $offset" }
        annotatedText.getStringAnnotations(
            tag = "URL", start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            logger { "Clicked URL: ${annotation.item}" }
        }
    })
}

val notoSansFamily = FontFamily(
    Font(R.font.notosans_regular, FontWeight.Normal),
    Font(R.font.notosans_medium, FontWeight.Medium),
    Font(R.font.notosans_semi_bold, FontWeight.SemiBold),
    Font(R.font.notosans_bold, FontWeight.Bold),
    Font(R.font.notosans_extra_bold, FontWeight.ExtraBold),
)

@Composable
fun DifferentFonts() {
    Column {
        Text("Hello World", fontFamily = notoSansFamily, fontWeight = FontWeight.Normal)
        Text("Hello World", fontFamily = notoSansFamily, fontWeight = FontWeight.Medium)
        Text(text = "Hello World", fontFamily = notoSansFamily, fontWeight = FontWeight.Bold)
        Text(text = "Hello World", fontFamily = notoSansFamily, fontWeight = FontWeight.SemiBold)
        Text(text = "Hello World", fontFamily = notoSansFamily, fontWeight = FontWeight.ExtraBold)
        NotoSansMediumText(text = "NotoSansMediumText")
    }
}

@Composable
fun NotoSansMediumText(text: String){
    Text(text = text, fontFamily = notoSansFamily, fontWeight = FontWeight.Medium)
}
