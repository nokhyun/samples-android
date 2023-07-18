package com.nokhyun.passenger

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakePagingPassengerUseCase @Inject constructor(
    private val fakePagingRepository: FakePagingRepository
) {

    operator fun invoke(): Flow<PassengerEntity> {
        return fakePagingRepository.fetchPassenger()
    }
}