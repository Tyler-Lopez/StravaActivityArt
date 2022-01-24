package com.company.athleteapiart.data

import androidx.compose.ui.graphics.Color
import com.company.athleteapiart.ui.theme.Black
import com.company.athleteapiart.ui.theme.White

data class ActivitiesFormat(
    val backgroundColor: Color = Black,
    val activityColor: Color = White,
    val conditionallyFormat: Boolean = false,
)

enum class ConditionalFormatOption {
    DISTANCE,
}
