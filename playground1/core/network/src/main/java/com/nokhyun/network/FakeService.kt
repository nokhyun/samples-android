package com.nokhyun.network

import com.nokhyun.network.responseModel.TodoResponse
import retrofit2.http.GET

interface FakeService {

    @GET("todos/1")
    suspend fun todo(): TodoResponse
}