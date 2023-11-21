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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.nokhyun.exam_nav.ExamNavActivity
import com.nokhyun.uiexam.stateHolderExam.FavoriteFoodInput

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
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column {
            ModalBottomSheetExam()
//                ModalBottomSheetExam1()
//                ModalBottomSheetSample()

//                CanvasExam()
            FavoriteFoodInput(onFavoriteFoodInputChanged = {
                logger { "onFavoriteFoodInputChanged: $it" }
            })
        }
    }
}

fun logger(block: () -> Any) {
    Log.e("UiExam", block().toString())
}