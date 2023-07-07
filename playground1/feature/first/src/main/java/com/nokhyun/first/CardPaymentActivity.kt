package com.nokhyun.first

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CardPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Contents()
        }
    }

    @Composable
    private fun Contents() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
                .padding(24.dp)
        ) {
            Text(text = "")

        }
    }

    @Preview
    @Composable
    private fun ContentsPreview(
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            Text(text = "")
        }
    }

    @Composable
    fun OutlinedButton(
        text: String,
        onClick: () -> Unit
    ) {
        androidx.compose.material.OutlinedButton(
            onClick = onClick
        ) {
            Text(text = text)
        }
    }
}