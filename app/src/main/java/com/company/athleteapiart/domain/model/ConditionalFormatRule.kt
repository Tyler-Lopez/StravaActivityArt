package com.company.athleteapiart.domain.model

import androidx.compose.ui.graphics.Color

abstract class ConditionalFormatRule(
    var color: Color
) {
    abstract fun conditionMatched(value: Comparable<Any>): Boolean
}