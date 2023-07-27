package com.nokhyun.third.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nokhyun.third.AirlineUiState

@Composable
fun PassengerItem(
    modifier: Modifier,
    airline: AirlineUiState.Airline?,
    onClick: () -> Unit
) {
    airline?.also {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(
                    indication = CustomIndication,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                )
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
                if(airline.expended.value) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Divider(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp),
                        color = Color.Gray,
                        thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Expended!!!")
                }
            }
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