package com.nokhyun.fakepaging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nokhyun.passenger.FakePagingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FakePagingRepositoryImpl @Inject constructor(
    private val remoteDataSource: FakePagingRemoteDataSource
) : FakePagingRepository {
    override fun fetchPassenger(): Flow<PagingData<com.nokhyun.passenger.Passenger>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            PassengerPagingSource(remoteDataSource)
        }.flow
    }
}