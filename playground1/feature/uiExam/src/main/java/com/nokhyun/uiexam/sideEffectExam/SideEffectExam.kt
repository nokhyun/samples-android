package com.nokhyun.uiexam.sideEffectExam

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nokhyun.uiexam.logger

@Composable
fun SideEffectScreen() {
    var sideEffectState by remember { mutableStateOf(true) }
    val analytics = rememberAnalytics()

    if (sideEffectState) analytics.setUserProperty("key1", "value1")

    Button(onClick = {
        sideEffectState = !sideEffectState
    }) {
        Text(text = "상태변경")
    }
}

@Composable
fun rememberAnalytics(): Analytics {
    val analytics = remember { Analytics() }

    /*
    * Compose state 를 Compose 에서 관리하지 않는 개체와 공유하려면 recomposition 마다 호출이 되는 SideEffect 가 있다.
    * 현재 컴포지션이 성공적으로 완료되고 변경 사항이 적용되면 효과가 실행되도록 예약합니다.
    * 성공적인 재구성이 발생한 경우에만 호출되어야 하는 작업에 사용
    * 모든 노드 트리에서 공통적으로 호출되어야할 때
    * */
    SideEffect {
        logger { "SideEffect !!!" }
        analytics.setUserProperty("userKey", "type")
    }

    return analytics
}

class Analytics {
    fun setUserProperty(key: String, value: String) {
        logger { "key: $key :: value: $value" }
    }
}