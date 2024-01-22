package com.nokhyun.uiexam.produceStateExam

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState

@Composable
fun ProduceStateScreen() {
    /*
    * nonState -> State
    * 컴포지션을 시작하면 프로듀서가 실행되고 종료하면 취소된다.
    * 코루틴을 만드는 경우에도 정지되지 않는 데이터 소스를 관찰하는 데 사용할 수 있음
    * */
    val state = produceState<ProduceResult<User>>(initialValue = ProduceResult.Loading) {
        val user = User(id = 1, name = "kim")
        this.value = if (user.id != 1) {
            ProduceResult.Error(Throwable("ProduceResult.Error"))
        } else {
            ProduceResult.Success(user)
        }

        awaitDispose {
            // 해당 소스의 구독을 삭제 필요 시 사용.
        }
    }

    when (val result = state.value) {
        ProduceResult.Loading -> {
            "loading"
        }

        is ProduceResult.Error -> {
            result.exception.message ?: "error"
        }

        is ProduceResult.Success<User> -> {
            result.getOrNull?.name
        }
    }?.also {
        Text(text = it)
    }
}

private sealed class ProduceResult<out T> {
    data object Loading : ProduceResult<Nothing>()
    data class Success<R>(
        private val value: R?,
    ) : ProduceResult<R>() {
        val getOrNull: R?
            get() {
                return this.value
            }
    }

    data class Error(
        val exception: Throwable,
    ) : ProduceResult<Nothing>()
}

private data class User(
    val id: Int,
    val name: String,
)