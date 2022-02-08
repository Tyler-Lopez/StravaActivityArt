package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.data.DistanceCondition
import com.company.athleteapiart.data.DistanceRule
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.meterToMiles
import com.company.athleteapiart.util.milesToMeters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatScreenFourViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var currRule = mutableStateOf(0)
    val rules = AthleteActivities.formatting.value.conditions
    var rulesSize = mutableStateOf(
        AthleteActivities.formatting.value.conditions.size
    )
    var distanceSlider = mutableStateOf(0f)
    var distanceCondition = mutableStateOf(DistanceCondition.LESS_THAN)
    var maxDistance = mutableStateOf(0f)
    var red = mutableStateOf(255)
    var green = mutableStateOf(255)
    var blue = mutableStateOf(255)


    init {
        if (rules.isEmpty())
            rules.add(
                DistanceRule(
                    3.1,
                    DistanceCondition.LESS_THAN,
                    color = Color.Red
                )
            )

        for (activity in AthleteActivities.filteredActivities.value) {
            if (activity.distance > maxDistance.value)
                maxDistance.value = activity.distance.toFloat()
        }

        updateValues()
    }

    private fun updateValues() {
        red.value = (rules[currRule.value].color.red * 255).toInt()
        green.value = (rules[currRule.value].color.green * 255).toInt()
        blue.value = (rules[currRule.value].color.blue * 255).toInt()
        if (rules[currRule.value] is DistanceRule) {
            distanceCondition.value = (rules[currRule.value] as DistanceRule)
                .distanceCondition
            distanceSlider.value = (rules[currRule.value] as DistanceRule)
                .conditionValue
                .milesToMeters()
                .toFloat()
        }
        rulesSize.value = AthleteActivities.formatting.value.conditions.size
    }

    fun setCurrRule(i: Int) {
        currRule.value = i
        updateValues()
    }

    fun incrementRule() {
        rules.add(
            DistanceRule(
                3.1,
                DistanceCondition.LESS_THAN,
                color = Color.Red
            )
        )
        currRule.value++
        updateValues()
    }

    fun removeRule() {
        rules.removeAt(currRule.value)
        currRule.value--
        updateValues()
    }
    // Update color in ViewModel AND in specific rule
    fun changeColor(color: ColorChoice, value: Int) {
        when (color) {
            ColorChoice.RED -> {
                red.value = value
                rules[currRule.value].color = Color(
                    value / 255f,
                    rules[currRule.value].color.green,
                    rules[currRule.value].color.blue,
                )
            }
            ColorChoice.GREEN -> {
                green.value = value
                rules[currRule.value].color = Color(
                    rules[currRule.value].color.red,
                    value / 255f,
                    rules[currRule.value].color.blue,
                )
            }
            ColorChoice.BLUE -> {
                blue.value = value
                rules[currRule.value].color = Color(
                    rules[currRule.value].color.red,
                    rules[currRule.value].color.green,
                    value / 255f,
                )
            }
        }

    }

    // DISTANCE RELATED SPECIFICALLY

    // Set distance in ViewModel AND condition
    fun setDistanceSlider(distance: Float) {
        distanceSlider.value = distance
        (rules[currRule.value] as DistanceRule)
            .conditionValue = distance
            .toDouble()
            .meterToMiles()
    }

    fun setDistanceCondition(condition: DistanceCondition) {
        distanceCondition.value = condition
        (rules[currRule.value] as DistanceRule)
            .distanceCondition = condition
    }

    enum class ColorChoice {
        RED, GREEN, BLUE;
    }
}
