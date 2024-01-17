package com.nokhyun.uiexam.text

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MyTextScreen() {
    val (value, onValueChanged) = remember { mutableStateOf("") }
    val processedValue by remember(value) { derivedStateOf { value.filter { !it.isDigit() } } }

    MyTextField(
        value = processedValue,
        onValueChanged = onValueChanged
    )
}

@Composable
fun MyTextField(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChanged
    )
}