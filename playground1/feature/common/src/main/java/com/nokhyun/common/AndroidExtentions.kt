package com.nokhyun.common

import android.animation.Animator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Animator.awaitEnd() = suspendCancellableCoroutine<Unit> { cont ->
    cont.invokeOnCancellation { cancel() }

    addListener(object : Animator.AnimatorListener {
        private var endedSuccessfully = true

        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
            animation.removeListener(this)

            if (cont.isActive) {
                if (endedSuccessfully) {
                    cont.resume(Unit) {}
                } else {
                    cont.cancel()
                }
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            endedSuccessfully = false
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    })
}