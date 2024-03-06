package com.nokhyun.exam_nav

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal sealed interface ContainerState {
    data object Fab : ContainerState
    data object FullScreen : ContainerState
}

@Composable
fun FabContent(
    modifier: Modifier
) {
    var containerState: ContainerState by remember { mutableStateOf(ContainerState.Fab) }
    val transition = updateTransition(targetState = containerState, label = null)
    val cornerRadius by animateDpAsState(
        targetValue = when (containerState) {
            ContainerState.Fab -> 22.dp
            ContainerState.FullScreen -> 0.dp
        }, label = ""
    )
    val backgroundColor by animateColorAsState(
        targetValue = when (transition.currentState) {
            ContainerState.Fab -> Color.LightGray
            ContainerState.FullScreen -> Color.Gray
        },
        label = ""
    )

    transition.AnimatedContent(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind {
                drawRect(backgroundColor)
            },
    ) { state ->
        when (state) {
            is ContainerState.Fab -> {
                Fab(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                ) { containerState = ContainerState.FullScreen }
            }

            is ContainerState.FullScreen -> {
                FullScreen(
                    modifier = Modifier
                ) { containerState = ContainerState.Fab }
            }
        }
    }
}

@Composable
fun Fab(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onClick
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}

@Composable
fun FullScreen(
    modifier: Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Toolbar(onBack)
    }
}

@Composable
fun Toolbar(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = null,
            modifier = Modifier.clickable { onBack() }
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Title",
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun FullScreenPreview() {
    FullScreen(modifier = Modifier) {

    }
}