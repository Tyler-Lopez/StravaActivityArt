package com.activityartapp.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.textField.MediumEmphasisTextField
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.FilterSectionDistances(
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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (distanceSelected ?: distanceTotal).let {
                MediumEmphasisTextField(
                    modifier = Modifier.weight(1f, false),
                    text = "%.2f".format(it.start.meterToMiles()) + " mi",
                    label = "Shortest"
                ) {

                }
                MediumEmphasisTextField(
                    modifier = Modifier.weight(1f, false),
                    text = "%.2f".format(it.endInclusive.meterToMiles()) + " mi",
                    label = "Longest"
                ) {

                }
            }
        }
        RangeSlider(
            value = adjDistanceSelected ?: adjDistanceTotal,
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
