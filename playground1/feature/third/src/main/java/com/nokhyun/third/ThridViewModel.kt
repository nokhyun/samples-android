package com.nokhyun.third

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

    val result: Flow<PagingData<AirlineUiState>> = passengerUseCase()
        .map {
            it.flatMap {
                it.airline
            }.map {
                it.asAirLine()
            }
        }
        .cachedIn(viewModelScope)

    private fun Airline.asAirLine(): AirlineUiState = AirlineUiState.Airline(
        name = this.name,
        country = this.country,
        logo = logo,
        slogan = slogan,
        headQuaters = head_quaters,
        website = website
    )
}

sealed class AirlineUiState {
    data class Airline(
        val name: String,
        val country: String,
        val logo: String,
        val slogan: String,
        val headQuaters: String,
        val website: String,
        var expended: MutableState<Boolean> = mutableStateOf(false)
    ) : AirlineUiState()
}

internal fun AirlineUiState.asAirline(): AirlineUiState.Airline? {
    return if (this is AirlineUiState.Airline) this else null
}