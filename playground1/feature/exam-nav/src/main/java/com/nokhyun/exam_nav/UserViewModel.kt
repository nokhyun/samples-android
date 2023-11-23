package com.nokhyun.exam_nav

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: String = checkNotNull(savedStateHandle["userId"])
}