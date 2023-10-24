package com.nokhyun.uiexam

import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BlurContents(
    modifier: Modifier,
    parent: ViewGroup
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_android_24),
                contentDescription = "background",
                contentScale = ContentScale.FillHeight
            )
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .border(
                    width = 2.dp,
                    color = Color.Yellow
                )
        ){
            BlurSurface(
                modifier = Modifier.size(240.dp),
                parent = parent
            )
        }
    }
}

@Composable
private fun BlurSurface(
    modifier: Modifier,
    parent: ViewGroup
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        AndroidView(
            factory = {
                BlurSurfaceView(parent.context)
            },
            modifier = Modifier
                .fillMaxSize(),
            update = { blurView ->
                blurView.setParent(parent = parent)
            }
        )
    }
}