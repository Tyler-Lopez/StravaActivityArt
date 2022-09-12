package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.company.activityart.domain.models.Activity

/**
 * @param yearMonthEarliest - Pair of year to 1-indexed month representing the earliest date
 * which an [Activity] has occurred.
 *
 * @param yearMonthLatest - Pair of year to 1-indexed month representing the latest date
 * which an [Activity] has occurred.
 */
@Composable
fun EditArtStandby(
    yearMonthEarliest: Pair<Int, Int>,
    yearMonthLatest: Pair<Int, Int>,
    distanceMax: Double,
    distanceMin: Double,
    selectedActivitiesCount: Int
) {
    Column {
        Text("Earliest $yearMonthEarliest")
        Text("Latest $yearMonthLatest")
        Text("$distanceMax")
        Text("$distanceMin")
        Text("$selectedActivitiesCount")
    }
}