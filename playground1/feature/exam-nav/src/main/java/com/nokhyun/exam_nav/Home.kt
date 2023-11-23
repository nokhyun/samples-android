package com.nokhyun.exam_nav

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 4.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = onClick
        ) {
            Text(text = "NavigateToTwo")
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 4.dp),
            onClick = onClick
        ) {
            Text(text = "NavigateToDeepLink")
        }
    }
}