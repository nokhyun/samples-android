package com.nokhyun.uiexam.text

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldCharSequence
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalFoundationApi::class)
fun Flow<TextFieldCharSequence>.isUserNameMatches(): Flow<Boolean> = this.mapLatest {
    !it.matches("""^[a-zA-Z가-힣]{2,16}$""".toRegex())
}

@OptIn(ExperimentalFoundationApi::class)
class BasicTextField2ExamViewModel : ViewModel() {

    val userName = TextFieldState()
    val userNameHasError: StateFlow<Boolean> = userName.textAsFlow()
        .debounce(500)
        .filter { it.isNotEmpty() }
        .isUserNameMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicTextField2Screen(
    viewModel: BasicTextField2ExamViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val errorState by viewModel.userNameHasError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Title: BasicTextFieldExample",
            fontSize = 28.sp,
        )

        BasicTextField2(
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
            state = viewModel.userName,
            lineLimits = TextFieldLineLimits.SingleLine
        )

        if (errorState) {
            Text(
                text = "Error",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Red,
                fontStyle = FontStyle.Italic
            )
        }
    }
}