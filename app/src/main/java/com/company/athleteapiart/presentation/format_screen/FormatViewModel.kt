package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var backgroundColorRed = mutableStateOf(0)
    var backgroundColorGreen = mutableStateOf(0)
    var backgroundColorBlue = mutableStateOf(0)

    var activityColorRed = mutableStateOf(255)
    var activityColorGreen = mutableStateOf(255)
    var activityColorBlue = mutableStateOf(255)

    var useConditionalFormatting = mutableStateOf(false)

}