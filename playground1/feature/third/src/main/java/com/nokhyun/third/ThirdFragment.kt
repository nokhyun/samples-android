package com.nokhyun.third

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nokhyun.third.composable.ThirdNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                TestDialog(
                    onDismissRequest = { },
                    context = LocalContext.current
                    ) {
                    Text(text = "asdl;k;jasdlkjaskl")
                }
                ThirdNavHost()
            }
        }
    }
}


// test
@Composable
fun TestDialog(
    onDismissRequest: () -> Unit,
    context: Context,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    // test xml
//    val localView = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val composeView = ComposeView(context).apply {
        setContent(content = content)
    }

    val dialog = remember(composeView) {
        DialogWrapper(composeView, lifecycleOwner).apply {
            setContentView(composeView)
        }
    }

    DisposableEffect(dialog) {
        dialog.show()
        onDispose {
            dialog.dismiss()
        }
    }

    dialog.show()

//
//    val dialog = remember {
//        DialogWrapper(view)
//    }

//    DisposableEffect(dialog){
//
//        onDispose {
//            dialog.dismiss()
//        }
//    }
//    BottomSheetDialog(LocalContext.current).show()
//    ComponentDialog(
//        LocalContext.current
//    ).show()
}

private class DialogWrapper(
    private val composeView: View,
    private val lifecycleOwner: LifecycleOwner
) : BottomSheetDialog(composeView.context) {

    override fun setContentView(view: View) {
        super.setContentView(view)
        view.findViewById<FrameLayout>(com.google.android.material.R.id.container).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
        }
    }
}

