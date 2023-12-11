package com.nokhyun.network.responseModel

import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
