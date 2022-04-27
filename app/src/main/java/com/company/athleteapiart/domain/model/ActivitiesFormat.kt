package com.company.athleteapiart.domain.model

import androidx.compose.ui.graphics.Color
import com.company.athleteapiart.presentation.ui.theme.Black
import com.company.athleteapiart.presentation.ui.theme.White

data class ActivitiesFormat(
    var leftString: String = "ATHLETE NAME",
    var rightString: String = "YEAR",
    var backgroundColor: Color = Black,
    var activityColor: Color = White,
    var conditionallyFormat: Boolean = false,
    var conditions: MutableList<ConditionalFormatRule> = mutableListOf()
)




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


