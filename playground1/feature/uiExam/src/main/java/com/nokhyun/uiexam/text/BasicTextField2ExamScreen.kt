package com.nokhyun.uiexam.text

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.TextFieldBuffer
import androidx.compose.foundation.text2.input.TextFieldCharSequence
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.allCaps
import androidx.compose.foundation.text2.input.forEachTextValue
import androidx.compose.foundation.text2.input.maxLengthInChars
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.foundation.text2.input.then
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.nokhyun.uiexam.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalFoundationApi::class)
fun Flow<TextFieldCharSequence>.isUserNameMatches(): Flow<Boolean> = this.mapLatest {
    if (it.isEmpty()) return@mapLatest false

    !it.matches("""^[a-zA-Z가-힣]{2,16}$""".toRegex())
}

@OptIn(ExperimentalFoundationApi::class)
class BasicTextField2ExamViewModel : ViewModel() {

    val userName = TextFieldState()
    val userNameHasError: StateFlow<Boolean> = userName.textAsFlow()
        .debounce(500)
        .isUserNameMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    var userNameHasError2 by mutableStateOf(false)

    suspend fun verifyUsername() {
        userName.forEachTextValue {
            userNameHasError2 = if (it.isEmpty()) {
                false
            } else {
                !it.matches("""^[a-zA-Z가-힣]{2,16}$""".toRegex())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicTextField2Screen(
    viewModel: BasicTextField2ExamViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onScrollDown: () -> Unit
) {
    val errorState by viewModel.userNameHasError.collectAsStateWithLifecycle()

    /* 방법2 */
    LaunchedEffect(Unit) {
        viewModel.verifyUsername()
    }

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
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = DigitOnlyTransformation()
                .then(InputTransformation.maxLengthInChars(6))
                .then(InputTransformation.allCaps(Locale.current))
        )
        
        if (errorState) {
//        if (viewModel.userNameHasError2) {
            onScrollDown()
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

/**
 *  입력순서(필터링 | 입력변환)
 * keyboard Input -> Input transformation -> TextField state -> output transformation -> visual output
 * */
@OptIn(ExperimentalFoundationApi::class)
class DigitOnlyTransformation : InputTransformation {

    override val keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Number)

    override fun transformInput(
        originalValue: TextFieldCharSequence,
        valueWithChanges: TextFieldBuffer
    ) {
        logger { "originalValue: $originalValue :: valueWithChanges: $valueWithChanges" }
        if (!valueWithChanges.asCharSequence().isDigitsOnly()) {
            valueWithChanges.revertAllChanges()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)

interface OutputTransformation {
    fun transformOutput(buffer: TextFieldBuffer)
}

@ExperimentalFoundationApi
object VerificationCodeOutputTransformation : OutputTransformation {
    override fun transformOutput(buffer: TextFieldBuffer) {
        logger { "transformOutput buffer: $buffer" }
        if (buffer.length > 6) return
        val padCount = 6 - buffer.length

        repeat(padCount) {
            buffer.append(".")
        }

        if (!buffer.asCharSequence().isDigitsOnly()) {
            buffer.revertAllChanges()
        }
    }
}