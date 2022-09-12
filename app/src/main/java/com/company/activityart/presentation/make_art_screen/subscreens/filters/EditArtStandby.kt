package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun EditArtStandby(
    distanceMax: Double,
    distanceMin: Double,
    selectedActivitiesCount: Int
) {
    Column {
        Text("$distanceMax")
        Text("$distanceMin")
        Text("$selectedActivitiesCount")
    }
}