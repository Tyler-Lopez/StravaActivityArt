package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.util.AthleteActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatScreenThreeViewModel @Inject constructor(
 //   private val repository: ActivityRepository
) : ViewModel() {
    val useConditionalFormatting = mutableStateOf(
        AthleteActivities
            .formatting
            .value
            .conditionallyFormat
    )

    var activityColorRed = mutableStateOf(255)
    var activityColorGreen = mutableStateOf(255)
    var activityColorBlue = mutableStateOf(255)

}
