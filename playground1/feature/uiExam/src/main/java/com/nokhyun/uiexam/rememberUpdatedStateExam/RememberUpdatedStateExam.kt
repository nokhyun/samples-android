package com.nokhyun.uiexam.rememberUpdatedStateExam

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.nokhyun.uiexam.logger
import kotlinx.coroutines.delay

@Composable
fun RememberUpdatedStateScreen() {
    var userText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = userText,
        onValueChange = {
            userText = it
        }
    )

    NumberData(userText) {
        logger { "timeOut: $it" }
    }
}

@Composable
fun NumberData(
    text: String,
    timeOut: (String) -> Unit,
) {
    /*
    * 값이 변경되었다고 다시시작하면 안될 때 사용.
    * 일정 시간을 기다렸다가 마지막의 값만 사용.
    * */
    val inputText = rememberUpdatedState(newValue = text)
    val timeOutState by rememberUpdatedState(newValue = timeOut)

    // 1회만 동작하며, rememberUpdatedState 코드에 의해 마지막 값만 표시됨.
    LaunchedEffect(true) {
        delay(3000L)
        timeOutState(inputText.value) // rememberUpdatedState를 사용하여 text 가 state로 변환되어 값이 갱신됨.
//        timeOutState(text) // state 가 아닌 일반타입으로 사용 시 값이 갱신되지 않음.
    }
}