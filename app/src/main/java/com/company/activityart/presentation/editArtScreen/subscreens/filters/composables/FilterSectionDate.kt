package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import android.app.DatePickerDialog
import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.editArtScreen.DateSelection
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay

/**
 * @param dateMaxDateSelectedYearMonthDay When null, use total value in place of selected.
 * @param dateMinDateSelectedYearMonthDay When null, use total value in place of selected.
 */
@Composable
fun FilterSectionDate(
    count: Int,
    dateSelections: List<DateSelection>,
    dateMaxDateSelectedYearMonthDay: YearMonthDay?,
    dateMinDateSelectedYearMonthDay: YearMonthDay?,
    dateMaxDateTotalYearMonthDay: YearMonthDay,
    dateMinDateTotalYearMonthDay: YearMonthDay,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val beforeDatePickerDialog =
        (dateMaxDateSelectedYearMonthDay ?: dateMaxDateTotalYearMonthDay).run {
            DatePickerDialog(LocalContext.current).apply {
                datePicker.updateDate(year, month, day)
                setOnDismissListener {
                    // Force cancel to reset active date
                    datePicker.updateDate(year, month, day)
                }
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    eventReceiver.onEvent(
                        EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDateChanged.FilterBeforeChanged(
                            YearMonthDay(year, month, dayOfMonth).unixMsLast
                        )
                    )
                }
                datePicker.maxDate = dateMaxDateTotalYearMonthDay.unixMs
                datePicker.minDate = dateMinDateTotalYearMonthDay.unixMs
            }
        }
    val afterDatePickerDialog =
        (dateMinDateSelectedYearMonthDay ?: dateMinDateTotalYearMonthDay).run {
            DatePickerDialog(LocalContext.current).apply {
                datePicker.updateDate(year, month, day)
                setOnDismissListener {
                    // Force cancel to reset active date
                    datePicker.updateDate(year, month, day)
                }
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    eventReceiver.onEvent(
                        EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDateChanged.FilterAfterChanged(
                            YearMonthDay(year, month, dayOfMonth).unixMsFirst
                        )
                    )
                }
                datePicker.maxDate = dateMaxDateTotalYearMonthDay.unixMs
                datePicker.minDate = dateMinDateTotalYearMonthDay.unixMs
            }
        }
    FilterSection(
        count = count,
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        dateSelections.forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = false,
                    onClick = {
                    }
                )
                when (it) {
                    is DateSelection.Year -> Subhead(text = "${it.year}")
                    is DateSelection.Custom -> {}
                }
            }
        }
        /*
        dateYears.forEach {

                RadioButton(
                    selected = false,
                    onClick = {
                    }
                )

            }
        }

         */
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.medium)
            ) {
                Subhead(text = stringResource(R.string.edit_art_filters_date_start))
                MediumEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = "${dateMinDateSelectedYearMonthDay ?: dateMinDateTotalYearMonthDay}"
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
                    text = "${dateMaxDateSelectedYearMonthDay ?: dateMaxDateTotalYearMonthDay}"
                ) {
                    beforeDatePickerDialog.show()
                }
            }
        }
    }
}