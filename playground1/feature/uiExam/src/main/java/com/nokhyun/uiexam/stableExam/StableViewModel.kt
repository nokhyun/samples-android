package com.nokhyun.uiexam.stableExam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.nokhyun.uiexam.logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StableViewModel : ViewModel() {
    private val _value: MutableStateFlow<Int> = MutableStateFlow(0)
    val value = _value.asStateFlow()

    fun plusValue() {
        _value.value += 1
    }
}

@Composable
fun StableComposable(
    viewModel: StableViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val value by viewModel.value.collectAsState()

    /*
    * ViewModel 안드로이드 의존성을 최소화 하기위해 @Stable 어노테이션을 사용할 수는 없음.
    * */
//    StableComposable(value) {
//        viewModel.plusValue()
//    }

    // 해결책 1
    StableComposable(value, viewModel::plusValue) // reference 전달

    // 해결책 2
    val onClick by remember { mutableStateOf({ viewModel.plusValue() }) }
//    StableComposable(value) { onClick() }

    // 해결책 3 (이건 그냥 내가 억지로 만들어봄...) 해결책 1번에서 1 depth 더 추가되는거라 딱히...
//    val stableModel = StableModel(viewModel::plusValue)
//    StableComposable(value, stableModel::onClick.invoke())
}

@Composable
fun StableComposable(value: Int, onClick: () -> Unit) {
    logger { "onClick: ${onClick.hashCode()}" }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.Black),
        text = value.toString(),
        fontSize = 24.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color.White
    )
}

data class StableModel(
    val onClick: () -> Unit,
)
