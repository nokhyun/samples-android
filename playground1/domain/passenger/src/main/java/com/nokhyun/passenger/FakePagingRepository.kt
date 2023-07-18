package com.nokhyun.passenger

import kotlinx.coroutines.flow.Flow

interface FakePagingRepository {
    fun fetchPassenger(): Flow<PassengerEntity>
}