package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleFour
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.util.YearMonthDay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSectionDate(
    dateRangeSecondsSelected: ClosedFloatingPointRange<Float>,
    dateRangeSecondsSelectedYMDStart: YearMonthDay,
    dateRangeSecondsSelectedYMDEnd: YearMonthDay,
    dateRangeSecondsTotal: ClosedFloatingPointRange<Float>,
    dateRangeYearsSelected: ClosedFloatingPointRange<Float>,
    dateRangeYearsTotal: ClosedFloatingPointRange<Float>,
    eventReceiver: EventReceiver<EditArtFiltersViewEvent>
) {
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        TitleFour("Years")
        Subhead(text = "2021 to 2022")
        RangeSlider(
            values = dateRangeYearsSelected,
            onValueChange = {
                /*
                eventReceiver.onEvent(
                    EditArtViewEvent.FilterDateChanged(
                        newUnixSecondStart = it.start,
                        newUnixSecondEnd = it.endInclusive
                    )
                )

                 */
            },
            valueRange = dateRangeYearsTotal,
        )
    }
}