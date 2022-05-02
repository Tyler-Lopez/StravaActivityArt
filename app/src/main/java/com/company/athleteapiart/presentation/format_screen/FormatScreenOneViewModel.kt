package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatScreenOneViewModel @Inject constructor(
//    private val repository: ActivityRepository
) : ViewModel() {

    var backgroundColorRed = mutableStateOf(0)
    var backgroundColorGreen = mutableStateOf(0)
    var backgroundColorBlue = mutableStateOf(0)

}