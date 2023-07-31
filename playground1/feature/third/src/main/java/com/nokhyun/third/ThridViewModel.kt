package com.nokhyun.third

import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ThirdViewModel @Inject constructor(
    passengerUseCase: FakePagingPassengerUseCase
) : ViewModel() {

    private val _detailScreen: MutableStateFlow<DetailScreenState> = MutableStateFlow(DetailScreenState.Default)
    val detailScreen: StateFlow<DetailScreenState> = _detailScreen.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailScreenState.Default)

    private fun <T> MutableStateFlow<T>.emitter(state: T) {
        viewModelScope.ensureActive()
        viewModelScope.launch {
            this@emitter.value = state
        }
    }

    fun detailScreen(state: DetailScreenState) {
        _detailScreen.emitter(state)
    }

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

internal sealed class DetailScreenState {
    object Default : DetailScreenState()
    object Detail : DetailScreenState()
}