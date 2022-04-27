package com.company.athleteapiart.domain.model

import androidx.compose.ui.graphics.Color

// Match on less than, equal to, or greater than a given amount of miles
class DistanceRule(
    var conditionValue: Double,
    var distanceCondition: DistanceCondition,
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
