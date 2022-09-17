package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.*
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay
import java.time.YearMonth

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
            setOnDismissListener {
                // Force cancel to reset active date
                datePicker.updateDate(year, month, day)
            }
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
            setOnDismissListener {
                // Force cancel to reset active date
                datePicker.updateDate(year, month, day)
            }
            datePicker.maxDate = dateYearMonthDayBefore.unixMilliseconds
            datePicker.minDate = dateMinDateUnixMilliSeconds
        }
    }
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.medium)
            ) {
                Subhead(text = stringResource(R.string.edit_art_filters_date_start))
                MediumEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = "$dateYearMonthDayAfter"
                ) {
                    afterDatePickerDialog.show()
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.medium)
            ) {
                Subhead(text = stringResource(R.string.edit_art_filters_date_end))
                MediumEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = "$dateYearMonthDayBefore"
                ) {
                    beforeDatePickerDialog.show()
                }
            }
        }
    }
}