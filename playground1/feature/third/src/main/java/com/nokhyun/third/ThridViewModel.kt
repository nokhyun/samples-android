package com.nokhyun.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import androidx.paging.map
import com.nokhyun.passenger.Airline
import com.nokhyun.passenger.FakePagingPassengerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ThirdViewModel @Inject constructor(
    passengerUseCase: FakePagingPassengerUseCase
) : ViewModel() {

    val result: Flow<PagingData<com.nokhyun.third.composable.Airline>> = passengerUseCase()
        .map {
            it.flatMap {
                it.airline
            }.map {
                it.asAirLine()
            }
        }
        .cachedIn(viewModelScope)

    private fun Airline.asAirLine() = com.nokhyun.third.composable.Airline(
        name = this.name,
        country = this.country,
        logo = logo,
        slogan = slogan,
        headQuaters = head_quaters,
        website = website
    )
}