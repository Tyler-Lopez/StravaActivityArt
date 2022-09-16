package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleFour
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import java.time.YearMonth

/**
 * @param dateSelectedEnd The [YearMonth] which filters all activities that occur
 * after it.
 * @param dateSelectedStart The [YearMonth] which filters all activities that occur
 * prior to it.
 * @param dateSecondsSelected The domain of selected seconds the athlete has chosen.
 * @param dateSecondsTotal The domain of all possible seconds the athlete may choose.
 * @param dateYearsSelectedCount The total number of years in the range of years
 * @param dateYearsSteps The number of steps, excluding start and end, present on
 * the years selection [RangeSlider].
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterSectionDate(
    dateSelectedEnd: YearMonth,
    dateSelectedStart: YearMonth,
    dateSecondsSelected: ClosedFloatingPointRange<Float>,
    dateSecondsTotal: ClosedFloatingPointRange<Float>,
    dateYearsSelectedCount: Int,
    dateYearsSteps: Int,
    eventReceiver: EventReceiver<EditArtFiltersViewEvent>
) {
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        TitleFour(text = stringResource(id = R.string.edit_art_filters_year_subheader))
        Subhead(
            text = pluralStringResource(
                id = R.plurals.edit_art_filters_year_description,
                count = dateYearsSelectedCount,
                formatArgs = arrayOf(
                    dateSelectedStart.year,
                    dateSelectedEnd.year
                )
            )
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.slider_horizontal_offset))
                .height(100.dp)) {
            drawRect(
                Color.Blue,
                Offset.Zero,
                this.size,
            )
        }
        RangeSlider(
            values = dateSecondsSelected,
            onValueChange = {
                eventReceiver.onEvent(
                    DateRangeYearsChanged(
                        newRange = it,
                        changeComplete = false
                    )
                )
            },
            onValueChangeFinished = {
                eventReceiver.onEvent(
                    DateRangeYearsChanged(
                        newRange = dateSecondsSelected,
                        changeComplete = true
                    )
                )
            },
            valueRange = dateSecondsTotal,
            steps = dateYearsSteps
        )
    }
}