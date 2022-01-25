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

    var rules = mutableStateListOf<ConditionalFormatRule>()

    fun newRule() {
        rules.add(
            DistanceRule(4.0, DistanceCondition.LESS_THAN, Color.Black)
        )
    }

}