package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toLowerCase
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterDateChanged.FilterAfterChanged
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterDateChanged.FilterBeforeChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay

@Composable
fun FilterSectionDate(
    dateMaxDateSelectedYearMonthDay: YearMonthDay,
    dateMinDateSelectedYearMonthDay: YearMonthDay,
    dateMaxDateTotalYearMonthDay: YearMonthDay,
    dateMinDateTotalYearMonthDay: YearMonthDay,
    selectedActivities: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val beforeDatePickerDialog = dateMaxDateSelectedYearMonthDay.run {
        DatePickerDialog(LocalContext.current).apply {
            datePicker.updateDate(year, month, day)
            setOnDismissListener {
                // Force cancel to reset active date
                datePicker.updateDate(year, month, day)
            }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                eventReceiver.onEvent(FilterBeforeChanged(YearMonthDay(year, month, dayOfMonth)))
            }
            datePicker.maxDate = dateMaxDateTotalYearMonthDay.unixMs
            datePicker.minDate = dateMinDateSelectedYearMonthDay.unixMs
        }
    }
    val afterDatePickerDialog = dateMinDateSelectedYearMonthDay.run {
        DatePickerDialog(LocalContext.current).apply {
            datePicker.updateDate(year, month, day)
            setOnDismissListener {
                // Force cancel to reset active date
                datePicker.updateDate(year, month, day)
            }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                eventReceiver.onEvent(FilterAfterChanged(YearMonthDay(year, month, dayOfMonth)))
            }
            datePicker.maxDate = dateMaxDateTotalYearMonthDay.unixMs
            datePicker.minDate = dateMinDateTotalYearMonthDay.unixMs
        }
    }
    FilterSection(
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
        filteredActivityCount = selectedActivities,
        // Todo, this isn't great.
        filterType = stringResource(R.string.edit_art_filters_date_header).lowercase()
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
                    text = "$dateMinDateSelectedYearMonthDay"
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
                    text = "$dateMaxDateSelectedYearMonthDay"
                ) {
                    beforeDatePickerDialog.show()
                }
            }
        }
    }
}