package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
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