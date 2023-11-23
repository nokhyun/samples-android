package com.nokhyun.exam_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

class ExamNavActivity : SetContentActivity() {

    override fun initContent(): @Composable () -> Unit = {
        val navHostController = rememberNavController()
        ExamNavHost(navHostController = navHostController)
    }
}