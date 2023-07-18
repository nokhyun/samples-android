package com.nokhyun.fakepaging

import android.util.Log
import com.nokhyun.fakepaging.PassengerMapper.toEntity
import com.nokhyun.passenger.FakePagingRepository
import com.nokhyun.passenger.PassengerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FakePagingRepositoryImpl(
    private val remoteDataSource: FakePagingRemoteDataSource
) : FakePagingRepository {
    override fun fetchPassenger(): Flow<PassengerEntity> {
        Log.e("fetchPassenger", "fetchPassenger")
        // TODO 단순 1회 호출로 API 호출 체크
        return remoteDataSource.fetchPassenger(
            passengerData = PassengerData(
                page = 0,
                size = 10
            )
        ).map { it.toEntity() }
    }
}