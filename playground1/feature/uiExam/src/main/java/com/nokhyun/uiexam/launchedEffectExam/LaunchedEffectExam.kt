package com.nokhyun.uiexam.launchedEffectExam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
* 컴포저블 내에서 코루틴을 사용해야할 경우..(suspend fun 등 )
* */
@Composable
fun LaunchedEffectScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    var hasError by remember { mutableStateOf(false) }

    Button(onClick = { hasError = !hasError }) {
        Text(text = "hasError Changed!!!")
    }

    if (hasError) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Hello",
                actionLabel = "Retry Message"
            )
        }
    }

    Scaffold(
        modifier = Modifier.height(200.dp),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ){
            Text(text = "Hello World")
        }
    }
}