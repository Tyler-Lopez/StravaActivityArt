package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.data.ConditionalFormatRule
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.data.DistanceRule
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatTwoViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activityColorRed = mutableStateOf(255)
    var activityColorGreen = mutableStateOf(255)
    var activityColorBlue = mutableStateOf(255)

    var useConditionalFormatting =
        mutableStateOf(AthleteActivities.formatting.value.conditionallyFormat)
}