package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.data.ConditionalFormatRule
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.data.DistanceRule
import com.company.athleteapiart.data.remote.responses.Athlete
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatScreenFourViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {
    var currentRule = mutableStateOf(1)
    val rules = AthleteActivities.formatting.value.conditions
    var distanceSlider = mutableStateOf(0f)
    var distanceCondition = mutableStateOf(DistanceCondition.LESS_THAN)
    var maxDistance = mutableStateOf(0f)
    var activityColorRed = mutableStateOf(255)
    var activityColorGreen = mutableStateOf(255)
    var activityColorBlue = mutableStateOf(255)



    init {
        if (rules.isEmpty())
            rules.add(
                DistanceRule(
                    3.1,
                    DistanceCondition.LESS_THAN,
                    color = Color.Red
                )
            )
        activityColorRed.value = (rules.first().color.red * 255).toInt()
        activityColorGreen.value = (rules.first().color.green * 255).toInt()
        activityColorBlue.value = (rules.first().color.blue * 255).toInt()

        for (activity in AthleteActivities.filteredActivities.value) {
            if (activity.distance > maxDistance.value)
                maxDistance.value = activity.distance.toFloat()
        }
    }

    fun setDistanceSlider(distance: Float) {
        distanceSlider.value = distance
    }


}
