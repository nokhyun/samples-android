package com.nokhyun.first

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class ControlFocusInsetsAnimationCallback(
    private val view: View,
    dispatchMode: Int = DISPATCH_MODE_STOP
) : WindowInsetsAnimationCompat.Callback(dispatchMode) {
    override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        if (animation.typeMask and WindowInsetsCompat.Type.ime() != 0) {
            view.post {
                checkFocus()
            }
        }
    }

    private fun checkFocus() {
        val imeVisible = ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime()) == true
        if (imeVisible && view.rootView.findFocus() == null) {
            view.requestFocus()
        } else if (!imeVisible && view.isFocusable) {
            view.clearFocus()
        }
    }
}