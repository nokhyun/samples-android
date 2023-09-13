package com.nokhyun.third.composable

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nokhyun.third.ThirdViewModel
import com.nokhyun.third.asAirline

@Composable
internal fun ThirdContent(
    thirdViewModel: ThirdViewModel,
    onNavigateScreen: () -> Unit,
) {
    val result = thirdViewModel.result.collectAsLazyPagingItems()
    val isVisible = result.loadState.refresh == LoadState.Loading

    val myAppState = rememberMyAppState()
    myAppState.myApp.state

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = isVisible
        ) {
            CircularProgressIndicator()
        }

        when (result.loadState.refresh) {
            !is LoadState.Error -> {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    userScrollEnabled = true
                ) {
                    items(result.itemCount) { index ->
                        result[index]?.also { item ->
                            PassengerItem(
                                modifier = Modifier,
                                airline = item.asAirline(),
                                onNavigateScreen = onNavigateScreen
                            ) {
                                item.asAirline()?.expended?.value = !item.asAirline()?.expended!!.value
                            }
                        }

                        if (!result.loadState.append.endOfPaginationReached) {
                            Divider(color = Color.Black, thickness = 1.dp)
                        }
                    }
                }
            }

            else -> {
                RetryScreen {
                    result.retry()
                }
            }
        }
    }
}

internal fun logger(log: () -> Any) {
    Log.e("logger", log().toString())
}

@Composable
internal fun composeLogger(log: @Composable () -> Any) {
    Log.e("logger", log().toString())
}

// 상태 보유자는 종속성의 수명이 동일하거나 더 짧은 한 다른 상태 보유자에 의존
@Composable
fun rememberMyAppState(
    myApp: MyApp = MyApp(),
): MyAppState = remember(myApp) {
    MyAppState(myApp)
}

@Stable
data class MyApp(
    val state: String = "State",
)

@Stable
data class MyAppState(
    val myApp: MyApp,
)
