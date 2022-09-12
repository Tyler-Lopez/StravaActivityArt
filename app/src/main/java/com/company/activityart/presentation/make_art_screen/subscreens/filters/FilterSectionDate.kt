package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.make_art_screen.EditArtViewEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSectionDate(
    unixSecondsRangeSelected: ClosedFloatingPointRange<Float>,
    unixSecondsRangeTotal: ClosedFloatingPointRange<Float>,
    eventReceiver: ViewEventListener<EditArtViewEvent>,
    eventReceiverFilters: ViewEventListener<EditArtFiltersViewEvent>
) {
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        RangeSlider(
            values = unixSecondsRangeSelected,
            onValueChange = {
                eventReceiverFilters.onEvent(
                    EditArtFiltersViewEvent.DistanceRangeChanged(
                        oldUnixSecondsRange = unixSecondsRangeTotal,
                        newUnixSecondsRange = it,
                        eventReceiverEditArt = eventReceiver
                    )
                )
            },
            valueRange = unixSecondsRangeTotal,
        )
    }
}