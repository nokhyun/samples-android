package com.nokhyun.exam_nav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

abstract class SetContentActivity : ComponentActivity() {

    abstract fun initContent(): @Composable () ->  Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(content = initContent())
    }
}
