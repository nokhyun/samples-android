package com.nokhyun.passenger

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakePagingPassengerUseCase @Inject constructor(
    private val fakePagingRepository: FakePagingRepository
) {

    operator fun invoke(): Flow<PassengerEntity> {
        Log.e("invoke", "invoke")
        return fakePagingRepository.fetchPassenger()
    }
}