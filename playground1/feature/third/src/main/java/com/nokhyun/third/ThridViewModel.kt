package com.nokhyun.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokhyun.passenger.FakePagingPassengerUseCase
import com.nokhyun.passenger.PassengerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThirdViewModel @Inject constructor(
    private val passengerUseCase: FakePagingPassengerUseCase
) : ViewModel() {

    val result: StateFlow<PassengerEntity?> = passengerUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}