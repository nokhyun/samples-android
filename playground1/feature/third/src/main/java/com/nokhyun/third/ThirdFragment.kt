package com.nokhyun.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.nokhyun.third.composable.ThirdContent
import com.nokhyun.third.composable.ThirdNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ThirdNavHost()
            }
        }
    }
}
