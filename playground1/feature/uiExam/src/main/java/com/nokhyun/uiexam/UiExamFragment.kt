package com.nokhyun.uiexam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

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

                ModalBottomSheetExam()
//                ModalBottomSheetExam1()
//                ModalBottomSheetSample()

                CanvasExam()
            }
        }
    }
}

fun logger(block: () -> Any) {
    Log.e("UiExam", block().toString())
}