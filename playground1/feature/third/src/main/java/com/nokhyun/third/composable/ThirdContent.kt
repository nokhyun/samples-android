package com.nokhyun.third.composable

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nokhyun.third.ThirdViewModel
import com.nokhyun.third.asAirline

@Composable
internal fun ThirdContent() {
    val thirdViewModel = viewModel<ThirdViewModel>()
    val result = thirdViewModel.result.collectAsLazyPagingItems()
    val isVisible = result.loadState.refresh == LoadState.Loading

    Box {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = isVisible
        ) {
            CircularProgressIndicator()
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            userScrollEnabled = true
        ) {
            items(result.itemCount) { index ->

                result[index]

                result[index]?.also { item ->
                    PassengerItem(
                        modifier = Modifier,
                        airline = item.asAirline()
                    ) {
                        // TODO result[index]
                        logger { "click!!!" }
                    }
                }

                if (!result.loadState.append.endOfPaginationReached) {
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }
    }
}

internal fun logger(log: () -> String) {
    Log.e("logger", log())
}
