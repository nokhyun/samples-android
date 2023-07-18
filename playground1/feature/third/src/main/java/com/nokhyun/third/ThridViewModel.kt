package com.nokhyun.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokhyun.passenger.FakePagingPassengerUseCase
import com.nokhyun.passenger.PassengerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThirdViewModel @Inject constructor(
    private val passengerUseCase: FakePagingPassengerUseCase
) : ViewModel() {

//    val resultFlow = flow<PassengerEntity> {
////        passengerUseCase()
//    }
//
//    init {
//        viewModelScope.launch {
//            resultFlow.collectLatest {
//                logger { it.toString() }
//            }
//        }
//    }
}