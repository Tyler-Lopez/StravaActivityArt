package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Paint
import android.graphics.Path
import com.google.android.gms.maps.model.LatLng

data class VisualizeSpecification(
    val visualizationWidth: Int,
    val visualizationHeight: Int,
    val backgroundPaint: Paint,
    val activityPaint: Paint,
    val activities: List<Path>
)
