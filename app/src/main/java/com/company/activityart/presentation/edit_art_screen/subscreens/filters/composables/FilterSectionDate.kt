package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.*
import com.company.activityart.util.classes.YearMonthDay
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
    dateMaxDateUnixMilliseconds: Long,
    dateMinDateUnixMilliSeconds: Long,
    dateYearMonthDayAfter: YearMonthDay,
    dateYearMonthDayBefore: YearMonthDay,
    eventReceiver: EventReceiver<EditArtFiltersViewEvent>
) {
    val beforeDatePickerDialog = dateYearMonthDayBefore.run {
        DatePickerDialog(
            LocalContext.current,
            { _, year, month, day ->
                eventReceiver.onEvent(DateChangedBefore(YearMonthDay(year, month, day)))
            },
            year,
            month,
            day
        ).apply {
            datePicker.maxDate = dateMaxDateUnixMilliseconds
            datePicker.minDate = dateYearMonthDayAfter.unixMilliseconds
        }
    }
    val afterDatePickerDialog = dateYearMonthDayAfter.run {
        DatePickerDialog(
            LocalContext.current,
            { _, year, month, day ->
                eventReceiver.onEvent(DateChangedAfter(YearMonthDay(year, month, day)))
            },
            year,
            month,
            day
        ).apply {
            datePicker.maxDate = dateYearMonthDayBefore.unixMilliseconds
            datePicker.maxDate = dateMinDateUnixMilliSeconds
        }
    }
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MediumEmphasisButton(
                size = ButtonSize.LARGE
            ) {

            }
        }

    }
}