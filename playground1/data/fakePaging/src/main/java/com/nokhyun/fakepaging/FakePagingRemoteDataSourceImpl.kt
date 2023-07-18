package com.nokhyun.fakepaging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal interface FakePagingRemoteDataSource {
    fun fetchPassenger(passengerData: PassengerData): Flow<PassengerResponse>
}

internal class FakePagingRemoteDataSourceImpl(
    private val fakePagingService: FakePagingService
) : FakePagingRemoteDataSource {
    override fun fetchPassenger(passengerData: PassengerData): Flow<PassengerResponse> = flow {
        emit(
            fakePagingService.fetchPassenger(
                mapOf(
                    KEY_PAGE to passengerData.page,
                    KEY_SIZE to passengerData.size
                )
            )
        )
    }

    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_SIZE = "size"
    }
}