package com.nokhyun.first

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class RootViewDeferringInsetsCallback(
    private val persistentInsetTypes: Int, // 레이아웃의 일부로 처리된 InsetType (항상 적용되는 InsetType)
    private val deferredInsetTypes: Int,   // 애니메이션이 끝날 때까지 연기되어야 하는 InsetType
) : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE), OnApplyWindowInsetsListener {

    private var view: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null

    private var deferredInsets = false

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        view = v
        lastWindowInsets = insets

        val types = when {
            deferredInsets -> persistentInsetTypes
            else -> persistentInsetTypes or deferredInsetTypes
        }

        val typeInsets = insets.getInsets(types)
        logger { "typeInsets: $typeInsets" }

        v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)

        return WindowInsetsCompat.CONSUMED
    }

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        if (animation.typeMask and deferredInsetTypes != 0) {
            deferredInsets = true
        }
    }

    override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        if (deferredInsets && (animation.typeMask and deferredInsetTypes) != 0) {
            deferredInsets = false

            if(lastWindowInsets != null && view != null){
                ViewCompat.dispatchApplyWindowInsets(view!!, lastWindowInsets!!)
            }
        }
    }
}