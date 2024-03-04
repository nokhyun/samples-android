package com.nokhyun.uiexam.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nokhyun.uiexam.logger

class CustomVisualTransformation : VisualTransformation {
    private val myOffsetMapping = MyOffsetMapping()

    override fun filter(text: AnnotatedString): TransformedText {
        log("filter")
        val transformText = text.toString()
            .mapIndexed { index, c ->
                "$c -"
            }
            .joinToString("")

        return TransformedText(
            text = AnnotatedString(text = transformText),
            offsetMapping = myOffsetMapping
        )
    }

    private fun log(msg: Any?) {
        logger { "[CustomVisualTransformation]:: ${msg.toString()}" }
    }
}

class MyOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        logger { "[originalToTransformed] offset: $offset" }
        if (offset <= 0) return offset
        return offset * 3
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0) return offset
        return offset / 2
    }
}

@Composable
fun VisualTransformationScreen() {
    var text by remember { mutableStateOf("") }
    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        visualTransformation = CustomVisualTransformation(),
    )
}