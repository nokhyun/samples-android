package com.nokhyun.uiexam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> Navigation(
    currentScreen: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    Box(modifier) {
        // `SaveableStateProvider` 안에 `currentScreen`을 나타내는 콘텐츠를 래핑합니다.
        // 여기에 Crossfade와 같은 화면 전환 애니메이션을 추가할 수도 있습니다.
        // 애니메이션 여러 화면이 동시에 표시됩니다.
        saveableStateHolder.SaveableStateProvider(key = currentScreen) {
            content(currentScreen)
        }
    }
}

@Composable
fun SaveableStateHolderExam(){
    Column {
        var screen by rememberSaveable { mutableStateOf("screen1") }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { screen = "screen1" }) {
                Text("Go to screen1")
            }
            Button(onClick = { screen = "screen2" }) {
                Text("Go to screen2")
            }
        }
        Navigation(screen, Modifier.fillMaxSize()) { currentScreen ->
            if (currentScreen == "screen1") {
                Screen1("screen1")
            } else {
                Screen2("screen2")
            }
        }
    }
}

/** 샘플이라 중복된 컴포저블 생성함 */
@Composable
fun Screen1(text: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}

@Composable
fun Screen2(text: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}
