package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.data.ConditionalFormatRule
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.data.DistanceRule
import com.company.athleteapiart.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatOneViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var backgroundColorRed = mutableStateOf(0)
    var backgroundColorGreen = mutableStateOf(0)
    var backgroundColorBlue = mutableStateOf(0)

    var useConditionalFormatting = mutableStateOf(false)

}