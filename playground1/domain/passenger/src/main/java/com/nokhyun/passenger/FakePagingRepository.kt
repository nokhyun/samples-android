package com.nokhyun.passenger

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FakePagingRepository {
    fun fetchPassenger(): Flow<PagingData<Passenger>>
}