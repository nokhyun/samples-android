package com.nokhyun.third.composable

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nokhyun.third.ThirdViewModel
import com.nokhyun.third.logger

@Composable
fun ThirdContent() {
    val thirdViewModel = viewModel<ThirdViewModel>()

    val result = thirdViewModel.result.collectAsState().value
    Log.e("result", result.toString())

    val list = mutableListOf<Airline>().apply {
        repeat(100) {
            add(
                Airline(
                    name = "Emirates",
                    country = "Dubai",
                    logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Emirates_logo.svg/150px-Emirates_logo.svg.png",
                    slogan = "From Dubai to destinations around the world.",
                    headQuaters = "Garhoud, Dubai, United Arab Emirates",
                    website = "www.emirates.com/",
                )
            )
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(list) { idx, item ->
            PassengerItem(
                modifier = Modifier,
                airline = item
            )

            if (idx < list.lastIndex) {
                Divider(color = Color.Black, thickness = 1.dp)
            }
        }
    }
}