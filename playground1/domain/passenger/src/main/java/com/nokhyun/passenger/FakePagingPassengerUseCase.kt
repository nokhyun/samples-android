package com.nokhyun.passenger

import android.util.Log
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakePagingPassengerUseCase @Inject constructor(
    private val fakePagingRepository: FakePagingRepository
) {

    operator fun invoke(): Flow<PagingData<Passenger>> {
        Log.e("invoke", "invoke")
        return fakePagingRepository.fetchPassenger()
    }
}