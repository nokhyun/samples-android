package com.nokhyun.common

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.common.databinding.ViewSnackbarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DefaultSnackBar(
    view: View,
    private val message: String,
    private val drawable: Drawable
) {

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 2000)

    @SuppressLint("RestrictedApi")
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    private val inflater = LayoutInflater.from(context)
    private val binding = ViewSnackbarBinding.inflate(inflater)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            val layoutParams = layoutParams as FrameLayout.LayoutParams

            CoroutineScope(Dispatchers.Main).launch { binding.root.fadeAnimation() }

            layoutParams.gravity = Gravity.CENTER
            removeAllViews()
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }

    }

    private fun initData() {
        binding.ivIcon.background = drawable
        binding.tvMessage.text = message
    }

    private suspend fun View.fadeAnimation() = suspendCoroutine {
        startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.anim_fade_in))

        postDelayed({
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_fade_out))
            it.resume(Unit)
        }, 2200)
    }

    fun show() {
        snackBar.show()
    }

    companion object {
        fun make(view: View, message: String, drawable: Drawable) = DefaultSnackBar(view, message, drawable)
    }
}