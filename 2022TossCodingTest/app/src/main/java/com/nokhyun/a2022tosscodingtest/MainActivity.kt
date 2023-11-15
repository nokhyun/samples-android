package com.nokhyun.a2022tosscodingtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nokhyun.a2022tosscodingtest.ui.theme._2022TossCodingTestTheme

/**
 * 토스 2022년 코테 기출문제
 * https://toss.im/career/article/next-developer-2023-sample-questions
 * */

class MainActivity : ComponentActivity() {

    private val solution = Solution()
    private val assets1 = arrayOf(
        "20-DE0815", "20-CO1201", "20-MO0901",
        "20-KE0511", "20-SP1102", "21-DE0401",
        "21-CO0404", "21-MO0794", "21-KE0704",
        "21-SP0404", "19-DE0401", "19-CO0404",
        "19-MO0794", "19-KE1204", "19-SP0404"
    )
    private val assets2 = arrayOf(
        "2-MO0915", // 유효하지 않은 자산 번호,
        "19-MO1299", "17-CO0901", "14-DE0511",
        "19-KE1102", "13-DE0101", "20-SP0404",
        "20-CO0794"
    )
    private val assets3 = arrayOf(
        "13-DE0401", "13-DE0401", "22-MO0815",
        "19-MO1299", "17-CO0901", "14-DE0511",
        "19-KE1102", "20-SP0404", "20-CO0794"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solution.solution(assets2)

        setContent {
            _2022TossCodingTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

fun logger(log: () -> Any?) {
    Log.e("logger:: ", log().toString())
}

enum class Code {
    SP, KE, MO, CO, DE
}

data class Asset(
    val value: String,
    val yy: Int,
    val code: Code,
    val mm: Int,
    val num: Int,
) : Comparable<Asset> {

    val isValidation: Boolean
        get() = when {
            (yy == 13 && mm in 4..12) ||
                    (yy in 14..21 && mm in 1..12) ||
                    (yy == 22 && mm in 1..8) -> true

            else -> false
        }

    override fun compareTo(other: Asset): Int {
        return when {
            other.yy < yy -> 1
            other.yy > yy -> -1
            other.code < code -> 1
            other.code > code -> -1
            other.mm < mm -> 1
            other.mm > mm -> -1
            other.num < num -> 1
            other.num > num -> -1
            else -> -0
        }
    }
}

class Solution {
    fun solution(assets: Array<String>): Array<String> {
        return assets.mapNotNull { asset ->
            runCatching { // runCatching 활용은 답안 체크함...
                /** extentions 활용하여 개선 가능. */
                when {
                    /**
                     *  Exception
                     *  object Validation: Throwable() 사용하여 개선 가능.
                     *  */
                    asset.length != 9 -> throw Exception("length: ${asset.length}")
                    asset.slice(0..1).toInt() !in 13..22 -> throw Exception("year: ${asset.slice(0..1)}")
                    /** asset.get(2) 로 개선 가능. */
                    asset.slice(2..2) != "-" -> throw Exception("asset slice 2 is not hyphen")
                    !Code.values().any { it.name == asset.slice(3..4) } -> throw Exception("asset code: ${asset.slice(3..4)}")
                    asset.slice(5..6).toInt() !in 1..12 -> throw Exception("mm: ${asset.slice(5..6)}")
                    asset.slice(7 until asset.length).toInt() > 99 -> throw Exception("num: ${asset.slice(7..asset.length)}")
                    else -> Asset(
                        value = asset,
                        yy = asset.slice(0..1).toInt(),
                        code = Code.valueOf(asset.slice(3..4)),
                        mm = asset.slice(5..6).toInt(),
                        num = asset.slice(7..8).toInt()
                    )
                }
            }.getOrNull()
        }.filter { it.isValidation }
            .sorted()
            .distinct()
            .map { it.value }
            .toTypedArray()
            .let {
                logger { "result: $it" }
                it
            }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _2022TossCodingTestTheme {
        Greeting("Android")
    }
}