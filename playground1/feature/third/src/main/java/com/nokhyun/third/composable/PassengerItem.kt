package com.nokhyun.third.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PassengerItem(
    modifier: Modifier,
    airline: Airline
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Black))
                .align(CenterVertically),
            model = airline.logo,
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Spacer(
            modifier = modifier
                .padding(start = 8.dp)
        )

        Column {
            Text(text = "name: ${airline.name}")
            Text(text = "country: ${airline.country}")
            Text(text = "slogan: ${airline.slogan}")
            Text(text = "head_quaters: ${airline.headQuaters}")
            Text(text = "website: ${airline.website}")
        }
    }
}

data class Airline(
    val name: String,
    val country: String,
    val logo: String,
    val slogan: String,
    val headQuaters: String,
    val website: String
)