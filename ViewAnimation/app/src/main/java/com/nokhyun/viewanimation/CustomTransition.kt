package com.nokhyun.viewanimation

import android.util.Log
import androidx.transition.Transition
import androidx.transition.TransitionValues

class CustomTransition: Transition() {

    private val PROPNAME_BACKGROUND = "com.example.android.customtransition:CustomTransition:background"

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues){
        Log.e(this.javaClass.simpleName, "captureValues")
        val view = transitionValues.view
        transitionValues.values[PROPNAME_BACKGROUND] = view.background
    }
}