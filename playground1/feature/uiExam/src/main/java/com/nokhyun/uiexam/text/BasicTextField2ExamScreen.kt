package com.nokhyun.uiexam.text

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.TextFieldBuffer
import androidx.compose.foundation.text2.input.TextFieldCharSequence
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.foundation.text2.input.forEachTextValue
import androidx.compose.foundation.text2.input.maxLengthInChars
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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

    val password = TextFieldState()
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BasicTextField2Screen(
    viewModel: BasicTextField2ExamViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onScrollDown: () -> Unit
) {
    val errorState by viewModel.userNameHasError.collectAsStateWithLifecycle()
    val textFieldColor = if (errorState) Color.Red else Color.Black

    val testState by rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(viewModel.userName)
    }

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
            text = "BasicTextFieldExample",
            fontSize = 28.sp,
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        BasicTextField2(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .border(
                    width = 1.dp,
                    color = textFieldColor,
                    shape = RoundedCornerShape(32.dp)
                ),
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = textFieldColor
            ),
            state = testState,
            lineLimits = TextFieldLineLimits.SingleLine,
//            inputTransformation = DigitOnlyTransformation()
//                .then(InputTransformation.maxLengthInChars(6)),
            decorator = { innerTextField ->
                val text = testState.text
                val interactionSource = remember { MutableInteractionSource() }
                val isValidationSuccess = testState.text.length > 1 && !errorState

                TextFieldDefaults.DecorationBox(
                    value = text.toString(),
                    placeholder = { Text(text = "Input your name, please.") },
                    enabled = false,
                    singleLine = true,
                    innerTextField = innerTextField,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                    trailingIcon = {
                        if (isValidationSuccess) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(32.dp)
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null)
                    },
                    container = {
//                        Text(text = "container!!!")
                    }
                )
            }
        )
        if (errorState) {
//        if (viewModel.userNameHasError2) {
            onScrollDown()
            Text(
                modifier = Modifier
                    .padding(start = 24.dp),
                text = "error",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Red,
                fontStyle = FontStyle.Italic
            )
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "BasicSecureTextField",
            fontSize = 28.sp,
        )

        BasicSecureTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(32.dp)
                ),
            state = viewModel.password,
            textObfuscationMode = TextObfuscationMode.RevealLastTyped,
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
            inputTransformation = InputTransformation.maxLengthInChars(12),
            decorator = { innerTextField ->
                val interactionSource = remember { MutableInteractionSource() }

                TextFieldDefaults.DecorationBox(
                    value = viewModel.password.text.toString(),
                    placeholder = { Text(text = "Input your password, please.") },
                    enabled = false,
                    singleLine = true,
                    innerTextField = innerTextField,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    },
                    container = {}
                )
            }
        )
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