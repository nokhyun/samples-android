package com.nokhyun.exam_nav

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TwoScreen(navHostController: NavHostController, string: String, userViewModel: UserViewModel) {
    Log.e("[TwoScreen]:: ", userViewModel.userId)
    Text(text = string)
}