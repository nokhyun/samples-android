package com.nokhyun.uiexam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class UiExamFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val decorView = requireActivity().window?.decorView
        val rootView = decorView?.findViewById<ViewGroup>(android.R.id.content)!!

        return ComposeView(requireContext()).apply {
            setContent {
//                UiExamContent()
                BlurContents(
                    modifier = Modifier,
                    parent = rootView
                )
            }
        }
    }
}

fun logger(block: () -> Any) {
    Log.e("UiExam", block().toString())
}