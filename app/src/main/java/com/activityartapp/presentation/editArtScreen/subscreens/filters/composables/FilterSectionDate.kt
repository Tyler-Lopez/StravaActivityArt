package com.activityartapp.presentation.editArtScreen.subscreens.filters.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.*
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.YearMonthDay

/**
 * @param dateMaxDateSelectedYearMonthDay When null, use total value in place of selected.
 * @param dateMinDateSelectedYearMonthDay When null, use total value in place of selected.
 */
@Composable
fun ColumnScope.FilterSectionDate(
    count: Int,
    dateSelections: List<DateSelection>,
    dateSelectionSelectedIndex: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val customDateSelection = dateSelections
        .firstOrNull { it is DateSelection.Custom } as? DateSelection.Custom
        ?: error("Missing DateSelection.Custom from dateSelections.")

    val rangeSelected = customDateSelection.dateSelected
    val rangeTotal = customDateSelection.dateTotal

    val ymdTotalStart = YearMonthDay.fromUnixMs(rangeTotal.first)
    val ymdTotalEnd = YearMonthDay.fromUnixMs(rangeTotal.last)

    val ymdSelectedStart = rangeSelected?.run { YearMonthDay.fromUnixMs(first) } ?: ymdTotalStart
    val ymdSelectedEnd = rangeSelected?.run { YearMonthDay.fromUnixMs(last) } ?: ymdTotalEnd

    val context = LocalContext.current

    val startDialog = ymdSelectedStart.run {
        DatePickerDialog(LocalContext.current).apply {
            datePicker.updateDate(year, month, day)
            // Force cancel to reset active date
            setOnDismissListener { datePicker.updateDate(year, month, day) }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                eventReceiver.onEvent(
                    FilterDateCustomChanged.FilterAfterChanged(
                        changedTo = YearMonthDay(
                            year,
                            month,
                            dayOfMonth
                        ).unixMsFirst
                    )
                )
            }
            datePicker.maxDate = ymdSelectedEnd.unixMs
            datePicker.minDate = ymdTotalStart.unixMs
        }
    }

    val endDialog = ymdSelectedEnd.run {
        DatePickerDialog(context).apply {
            datePicker.updateDate(year, month, day)
            // Force cancel to reset active date
            setOnDismissListener { datePicker.updateDate(year, month, day) }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                eventReceiver.onEvent(
                    FilterDateCustomChanged.FilterBeforeChanged(
                        changedTo = YearMonthDay(
                            year,
                            month,
                            dayOfMonth
                        ).unixMsLast
                    )
                )
            }
            datePicker.maxDate = ymdTotalEnd.unixMs
            datePicker.minDate = ymdSelectedStart.unixMs
        }
    }

    FilterSection(
        count = count,
        header = stringResource(R.string.edit_art_filters_date_header),
        description = stringResource(R.string.edit_art_filters_date_description),
    ) {
        dateSelections.forEachIndexed { index, selection ->
            RadioButtonContentRow(
                isSelected = index == dateSelectionSelectedIndex,
                text = when (selection) {
                    is DateSelection.All -> stringResource(R.string.edit_art_filters_date_include_all)
                    is DateSelection.Year -> selection.year.toString()
                    is DateSelection.Custom -> stringResource(R.string.edit_art_filters_date_custom_range)
                },
                content = {
                    if (selection is DateSelection.Custom) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(spacing.small),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selection.apply {
                                Button(
                                    emphasis = ButtonEmphasis.MEDIUM,
                                    modifier = Modifier.weight(1f, true),
                                    labelText = stringResource(R.string.edit_art_filters_date_start),
                                    size = ButtonSize.LARGE,
                                    text = ymdSelectedStart.toString()
                                ) {
                                    startDialog.show()
                                }
                                Button(
                                    emphasis = ButtonEmphasis.MEDIUM,
                                    modifier = Modifier.weight(1f, true),
                                    labelText = stringResource(R.string.edit_art_filters_date_end),
                                    size = ButtonSize.LARGE,
                                    text = ymdSelectedEnd.toString()
                                ) {
                                    endDialog.show()
                                }
                            }
                        }
                    }
                }
            ) {
                eventReceiver.onEvent(
                    FilterDateSelectionChanged(
                        index
                    )
                )
            }
        }
    }
}