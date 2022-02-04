package com.company.athleteapiart.data

import androidx.compose.ui.graphics.Color
import com.company.athleteapiart.ui.theme.Black
import com.company.athleteapiart.ui.theme.White

data class ActivitiesFormat(
    val backgroundColor: Color = Black,
    var activityColor: Color = White,
    var conditionallyFormat: Boolean = false,
    var conditions: MutableList<ConditionalFormatRule> = mutableListOf()
)

abstract class ConditionalFormatRule(
    var color: Color
) {
    abstract fun conditionMatched(value: Comparable<Any>): Boolean
}

// Match on less than, equal to, or greater than a given amount of miles
class DistanceRule(
    private val conditionValue: Double,
    private val distanceCondition: DistanceCondition,
    color: Color
) : ConditionalFormatRule(color) {
    override fun conditionMatched(value: Comparable<Any>): Boolean {
        return when (distanceCondition) {
            DistanceCondition.LESS_THAN -> value < conditionValue
            DistanceCondition.EQUAL_TO -> value.compareTo(conditionValue) == 0
            DistanceCondition.GREATER_THAN -> value > conditionValue
        }
    }
}

// Exact match to color by activity
class ActivityRule(
    private val conditionValue: String,
    color: Color
) : ConditionalFormatRule(color) {
    override fun conditionMatched(value: Comparable<Any>): Boolean =
        value.compareTo(conditionValue) == 0
}

// Exact match to color by month
class MonthRule(
    private val conditionValue: String,
    color: Color
) : ConditionalFormatRule(color) {
    override fun conditionMatched(value: Comparable<Any>): Boolean =
        value.compareTo(conditionValue) == 0
}

enum class DistanceCondition(
    val display: String,
) {
    LESS_THAN("Less than"),
    EQUAL_TO("Equal to"),
    GREATER_THAN("Greater than")
}


