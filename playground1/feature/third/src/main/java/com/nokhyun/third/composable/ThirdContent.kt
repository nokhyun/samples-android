package com.nokhyun.third.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nokhyun.third.ThirdViewModel

@Composable
fun ThirdContent() {
    val thirdViewModel = viewModel<ThirdViewModel>()
    val result = thirdViewModel.result.collectAsLazyPagingItems()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        userScrollEnabled = true
    ) {
        if (result.loadState.refresh == LoadState.Loading) {
            // TODO Loading
        }

        items(result.itemCount) {
            result[it]?.also { item ->
                PassengerItem(
                    modifier = Modifier,
                    airline = item
                )
            }

            if (!result.loadState.append.endOfPaginationReached) {
                Divider(color = Color.Black, thickness = 1.dp)
            }
        }
    }
}
