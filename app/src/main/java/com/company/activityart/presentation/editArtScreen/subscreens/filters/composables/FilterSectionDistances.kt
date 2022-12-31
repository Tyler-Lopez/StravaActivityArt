package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSectionDistances(
    count: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val adjDistanceSelected = distanceSelected?.run {
        start.toFloat()..endInclusive.toFloat()
    }
    val adjDistanceTotal = distanceTotal.start.toFloat()..distanceTotal.endInclusive.toFloat()
    FilterSection(
        count = count,
        header = stringResource(R.string.edit_art_filters_distance_header),
        description = stringResource(R.string.edit_art_filters_distance_description),
    ) {
        Text(
            text = (distanceSelected ?: distanceTotal).let {
                "${"%.2f".format(it.start.meterToMiles())} to ${"%.2f".format(it.endInclusive.meterToMiles())} mi"
            }
        )
        RangeSlider(
            values = adjDistanceSelected ?: adjDistanceTotal,
            modifier = Modifier.weight(2f, true),
            onValueChange = {
                eventReceiver.onEvent(
                    EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDistanceChanged(
                        it.run { start.toDouble()..endInclusive.toDouble() }
                    )
                )
            },
            valueRange = adjDistanceTotal
        )
    }
}

private fun Double.meterToMiles(): Double = this * 0.000621371192
