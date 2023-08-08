package com.nokhyun.first

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat


class TranslateDeferringInsetsAnimationCallback(
    private val view: View,
    private val persistentInsetTypes: Int,
    private val deferredInsetTypes: Int,
    dispatchMode: Int = DISPATCH_MODE_CONTINUE_ON_SUBTREE
) : WindowInsetsAnimationCompat.Callback(dispatchMode) {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
        val typesInset = insets.getInsets(deferredInsetTypes)
        val otherInset = insets.getInsets(persistentInsetTypes)

        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }

        view.translationX = (diff.left - diff.right).toFloat()
        view.translationY = (diff.top - diff.bottom).toFloat()

        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        view.translationX = 0f
        view.translationY = 0f
    }
}