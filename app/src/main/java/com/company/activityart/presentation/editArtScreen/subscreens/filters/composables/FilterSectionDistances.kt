package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.util.toRange
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay
import com.company.activityart.util.meterToMiles

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSectionDistances(
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val adjDistanceSelected = distanceSelected?.run {
        start.toFloat()..endInclusive.toFloat()
    }
    val adjDistanceTotal = distanceTotal.start.toFloat()..distanceTotal.endInclusive.toFloat()
    Section(
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
