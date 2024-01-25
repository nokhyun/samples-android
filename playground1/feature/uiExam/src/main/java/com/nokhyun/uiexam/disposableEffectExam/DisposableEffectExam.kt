package com.nokhyun.uiexam.disposableEffectExam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/*
* 키가 변경되거나 Composition 이 종료되고 그 후 필수로 삭제(정리)하는 기능이 필요 할 때 사용.
* */
@Composable
fun DisposableEffectScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val currentOnStart by rememberUpdatedState(newValue = onStart)
    val currentOnStop by rememberUpdatedState(newValue = onStop)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    currentOnStart()
                }

                Lifecycle.Event.ON_STOP -> {
                    currentOnStop()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}