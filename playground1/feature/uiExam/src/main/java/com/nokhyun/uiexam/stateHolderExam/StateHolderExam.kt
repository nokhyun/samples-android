package com.nokhyun.uiexam.stateHolderExam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.filter

@Composable
fun FavoriteFoodInput(onFavoriteFoodInputChanged: (String) -> Unit) {
    val foodEditableInputState = rememberFoodEditableInputState(hint = "Write your favorite food?")

    Surface(
        modifier = Modifier.fillMaxWidth(),
//        color = Color.Gray
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Gray)
        ) {
            Icon(Icons.Filled.Favorite, contentDescription = null)
            Spacer(modifier = Modifier.padding(4.dp))
            FoodEditableInput(
                modifier = Modifier.align(Alignment.CenterVertically),
                state = foodEditableInputState
            )
        }
    }

    val currentFavoriteFoodInputChanged by rememberUpdatedState(newValue = onFavoriteFoodInputChanged)
    LaunchedEffect(key1 = foodEditableInputState) {
        snapshotFlow { foodEditableInputState.text }
            .filter { !foodEditableInputState.isHint }
            .collect {
                currentFavoriteFoodInputChanged(foodEditableInputState.text)
            }
    }
}

@Composable
fun FoodEditableInput(
    modifier: Modifier,
    state: FoodEditableInputState = rememberFoodEditableInputState(hint = "")
) {
    BasicTextField(
        modifier = modifier,
        value = state.text,
        onValueChange = {
            state.text = it
        },
        textStyle = if (state.isHint) {
            MaterialTheme.typography.caption.copy(color = Color.LightGray)
        } else {
            MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
        },
        cursorBrush = SolidColor(LocalContentColor.current)
    )
}

class FoodEditableInputState(
    private val hint: String,
    initialText: String
) {
    var text by mutableStateOf(initialText)
    val isHint: Boolean get() = text == hint

    companion object {
        val Saver: Saver<FoodEditableInputState, *> = listSaver(
            save = { listOf(it.hint, it.text) },
            restore = {
                FoodEditableInputState(hint = it[0], initialText = it[1])
            }
        )
    }
}

@Composable
fun rememberFoodEditableInputState(hint: String): FoodEditableInputState = rememberSaveable(hint, saver = FoodEditableInputState.Saver) {
    FoodEditableInputState(hint, hint)
}

