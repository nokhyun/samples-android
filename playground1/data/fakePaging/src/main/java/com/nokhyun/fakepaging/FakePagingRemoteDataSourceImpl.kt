package com.nokhyun.fakepaging

import android.util.Log
import com.nokhyun.network_paging.FakePagingService
import com.nokhyun.network_paging.annotations.FakePaging
import com.nokhyun.network_paging.PassengerResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal interface FakePagingRemoteDataSource {
    fun fetchPassenger(passengerData: PassengerData): Flow<com.nokhyun.network_paging.PassengerResponse>
}

internal class FakePagingRemoteDataSourceImpl @Inject constructor(
    @com.nokhyun.network_paging.annotations.FakePaging private val fakePagingService: com.nokhyun.network_paging.FakePagingService
) : FakePagingRemoteDataSource {
    override fun fetchPassenger(passengerData: PassengerData): Flow<com.nokhyun.network_paging.PassengerResponse> = flow {
        Log.e("fetchPassenger", "fetchPassenger")
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