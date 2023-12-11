package com.nokhyun.network

import com.nokhyun.network.responseModel.PassengerResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FakePagingService {

    @GET("/v1/passenger")
    suspend fun fetchPassenger(
        @QueryMap passengerMap: Map<String, Int>,
    ): PassengerResponse
}