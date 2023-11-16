package com.nokhyun.uiexam.exams

import android.annotation.SuppressLint
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.nokhyun.uiexam.logger

data class User(val name: String)

@SuppressLint("CompositionLocalNaming")
val ActiveUser = compositionLocalOf<User> { error("No active User found!") }

@Composable
fun App(user: User = User(name = "Kim Hello World!")){
    // provides infix Operator
    // CompositionLocalProvider 호출 시 CompositionLocal 키를 값에 연결합니다.
    CompositionLocalProvider(ActiveUser provides user) {
        SomeScreen()
    }
}

@Composable
fun SomeScreen(){
    UserName()
}

@Composable
fun UserName(){
    val user = ActiveUser.current
    logger { "ActiveUser.current: $user" }
    Text(text = "name: ${user.name}")
}