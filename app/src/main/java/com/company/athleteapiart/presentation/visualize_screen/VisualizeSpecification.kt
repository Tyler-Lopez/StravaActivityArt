package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Paint
import android.graphics.Path

data class VisualizeSpecification(
    var visualizationWidth: Int,
    var visualizationHeight: Int,
    var backgroundPaint: Paint,
    var activityPaint: Paint,
    var activities: List<Path>
)
