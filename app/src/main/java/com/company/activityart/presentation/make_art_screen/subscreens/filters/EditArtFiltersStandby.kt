package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.make_art_screen.EditArtViewEvent
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.YearMonthDay

/**
 * @param yearMonthEarliest - Pair of year to 1-indexed month representing the earliest date
 * which an [Activity] has occurred.
 *
 * @param yearMonthLatest - Pair of year to 1-indexed month representing the latest date
 * which an [Activity] has occurred.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditArtStandby(
    dateEarliestSelected: YearMonthDay,
    dateLatestSelected: YearMonthDay,
    unixSecondsRangeSelected: ClosedFloatingPointRange<Float>,
    unixSecondsRangeTotal: ClosedFloatingPointRange<Float>,
    distanceMax: Double,
    distanceMin: Double,
    selectedActivitiesCount: Int,
    eventReceiver: ViewEventListener<EditArtViewEvent>,
    eventReceiverFilters: ViewEventListener<EditArtFiltersViewEvent>
) {

    FilterSectionDate(
        unixSecondsRangeSelected,
        unixSecondsRangeTotal,
        eventReceiver,
        eventReceiverFilters
    )
}