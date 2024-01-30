package com.nokhyun.uiexam.RememberCoroutineScope

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nokhyun.uiexam.logger
import kotlinx.coroutines.launch

@Composable
fun RememberCoroutineScopeScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    /*
    * 컴포저블 외부에서 코루틴을 실행이 필요할 때.
    * LaunchedEffect 도 사용 가능하지만, 컴포저블 외부(일반 함수)에서 사용불가능.
    * */
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.height(200.dp), // Column 내에서 사용중이라 height 설정.
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Show!",
                        actionLabel = "Action",
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            logger { "Dismissed" }
                        }

                        SnackbarResult.ActionPerformed -> {
                            logger { "ActionPerformed" }
                        }
                    }
                }
            }) {
                Text(text = "show SnackBar")
            }
        }
    }
}