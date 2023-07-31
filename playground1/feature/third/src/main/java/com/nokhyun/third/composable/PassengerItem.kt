package com.nokhyun.third.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nokhyun.third.AirlineUiState
import com.nokhyun.third.R

@Composable
fun PassengerItem(
    modifier: Modifier,
    airline: AirlineUiState.Airline?,
    onNavigateScreen: () -> Unit,
    onClick: () -> Unit
) {
    airline?.also {
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = onNavigateScreen)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(
                                1.dp,
                                Color.Blue
                            )
                        ),
                    model = airline.logo,
                    error = painterResource(id = R.drawable.ic_baseline_personal_video_24),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )

                Spacer(
                    modifier = modifier
                        .padding(start = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(0.8f),
                ) {
                    Column {
                        Text(text = "name: ${airline.name}")
                        Text(text = "country: ${airline.country}")
                        Text(text = "slogan: ${airline.slogan}")
                        Text(text = "head_quaters: ${airline.headQuaters}")
                        Text(text = "website: ${airline.website}")
                    }
                }

                Image(
                    modifier = Modifier
                        .rotate(if (airline.expended.value) 90f else 270f)
                        .clickable(
                            indication = CustomIndication,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClick
                        ),
                    contentScale = ContentScale.Inside,
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    alignment = Alignment.TopEnd
                )
            }

            AnimatedVisibility(visible = airline.expended.value) {
                Spacer(modifier = Modifier.padding(8.dp))
                Divider(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "Expended!!!")
            }
        }
    }
}

@Preview
@Composable
fun PassengerItemPreview() {
    PassengerItem(
        modifier = Modifier,
        airline = AirlineUiState.Airline(
            name = "name",
            country = "country",
            logo = "",
            slogan = "",
            headQuaters = "",
            website = ""
        ),
        onNavigateScreen = {},
        onClick = {}
    )
}

data class Airline(
    val name: String,
    val country: String,
    val logo: String,
    val slogan: String,
    val headQuaters: String,
    val website: String
)