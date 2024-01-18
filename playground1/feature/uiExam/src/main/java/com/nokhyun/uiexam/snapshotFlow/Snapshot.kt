package com.nokhyun.uiexam.snapshotFlow

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nokhyun.uiexam.logger
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SnapshotFlowScreen() {
    val listState = rememberLazyListState()
    buildList<Int> {
        for (i in 0 until 50) {
            add(i)
        }
    }.also { list ->
        LazyColumn(
            modifier = Modifier.height(300.dp), // 테스트 height 값 강제로 설정. (중첩 사용시 nestedScrollView + recyclerView 조합 사용 때 처럼 측정이 무한한 값으로 측정이 되며 recycle이 되지않음. 하지만 composable 이라 에러발생.)
            state = listState
        ) {
            item { Text(text = "Header") }
            items(list.size) {
                if(it % 5 == 0) Text(text = LocalDateTime.now().toString())
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    text = it.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }
            item { Text(text = "Footer") }
        }
    }

    LaunchedEffect(listState){
        // Flow 연산자의 이점을 활용할 수 있는 Flow로 변환
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 25 }
            .distinctUntilChanged()
            .filter { it }
            .collect{
                logger { "snapshotFlow listState greater than 25" }
            }
    }
}