package com.nokhyun.uiexam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.nokhyun.exam_nav.ExamNavActivity
import com.nokhyun.uiexam.RememberCoroutineScope.RememberCoroutineScopeScreen
import com.nokhyun.uiexam.disposableEffectExam.DisposableEffectScreen
import com.nokhyun.uiexam.draggableExam.DraggableScreen
import com.nokhyun.uiexam.immutableExam.StableTestScreen
import com.nokhyun.uiexam.launchedEffectExam.LaunchedEffectScreen
import com.nokhyun.uiexam.produceStateExam.ProduceStateScreen
import com.nokhyun.uiexam.rememberUpdatedStateExam.RememberUpdatedStateScreen
import com.nokhyun.uiexam.sideEffectExam.SideEffectScreen
import com.nokhyun.uiexam.snapshotFlow.SnapshotFlowScreen
import com.nokhyun.uiexam.stableExam.StableComposable
import com.nokhyun.uiexam.stateHolderExam.FavoriteFoodInput
import com.nokhyun.uiexam.text.MyTextScreen

class UiExamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val decorView = requireActivity().window?.decorView
        val rootView = decorView?.findViewById<ViewGroup>(android.R.id.content)!!

        return ComposeView(requireContext()).apply {
            setContent {
                val systemBar = WindowInsets.systemBars.getBottom(LocalDensity.current).dp
                logger { "systemBar: $systemBar" }

//                UiExamContent()
//                BlurContents(
//                    parent = rootView
//                )

                val window = requireActivity().window
                val view = requireView().rootView
                val navBarBottom = WindowInsets.navigationBars.getBottom(LocalDensity.current)
                var navBar by remember { mutableStateOf(navBarBottom) }
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        modifier = Modifier.weight(0.1f),
                        onClick = {
                            startActivity(Intent(requireActivity(), ExamNavActivity::class.java))
                        }) {
                        Text("NavHost")
                    }
                    ExamUI(
                        modifier = Modifier.weight(0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun ExamUI(
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            ModalBottomSheetExam()
//                ModalBottomSheetExam1()
//                ModalBottomSheetSample()

//                CanvasExam()
            FavoriteFoodInput(onFavoriteFoodInputChanged = {
                logger { "onFavoriteFoodInputChanged: $it" }
            })

            GraphicExam()
            AnimatedExam()
            StableTestScreen()
            StableComposable()
            SimpleClickableText()
            MyTextScreen()
            SnapshotFlowScreen()
            ProduceStateScreen()

            SideEffectScreen()
            DisposableEffectScreen(
                onStart = {
                    logger { "ON_START" }
                },
                onStop = {
                    logger { "ON_STOP" }
                }
            )

            RememberUpdatedStateScreen()
            RememberCoroutineScopeScreen()
            LaunchedEffectScreen()
            DraggableScreen()
        }
    }
}

/** 텍스트 클릭 위치 확인 */
@Composable
fun SimpleClickableText() {
    ClickableText(text = AnnotatedString("Click Me"), onClick = { offset ->
        Log.d("ClickableText", "$offset -th character is clicked.")
    })
}

fun logger(block: () -> Any) {
    Log.e("UiExam", block().toString())
}