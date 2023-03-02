package com.activityartapp.presentation.editArtScreen.subscreens.filters.sections

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.*
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterCount
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.YearMonthDay
import kotlin.random.Random

@Composable
fun SectionDate(
    count: State<Int>,
    dateSelections: SnapshotStateList<DateSelection>,
    dateSelectionSelectedIndex: State<Int>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    DateSelectionRowHandler(
        dateSelections,
        dateSelectionSelectedIndex.value,
        eventReceiver::onEvent,
        eventReceiver::onEvent,
        eventReceiver::onEvent
    )
    FilterCount(count.value)
}

@Composable
private fun DateSelectionRowHandler(
    dateSelections: List<DateSelection>,
    dateSelectionSelectedIndex: Int,
    onFilterDateSelectionChanged: (FilterDateSelectionChanged) -> Unit,
    onFilterAfterChanged: (FilterDateCustomChanged.FilterAfterChanged) -> Unit,
    onFilterBeforeChanged: (FilterDateCustomChanged.FilterBeforeChanged) -> Unit
) {
    dateSelections.forEachIndexed { index, selection ->
        val isSelected = index == dateSelectionSelectedIndex
        when (selection) {
            is DateSelection.All -> DateSelectionRowItemAll(
                isSelected = isSelected,
                index = index,
                onFilterDateSelectionChanged = onFilterDateSelectionChanged
            )
            is DateSelection.Custom -> DateSelectionRowItemCustom(
                isSelected = isSelected,
                index = index,
                selection = selection,
                onFilterDateSelectionChanged = onFilterDateSelectionChanged,
                onFilterAfterChanged = onFilterAfterChanged,
                onFilterBeforeChanged = onFilterBeforeChanged
            )
            is DateSelection.Year -> DateSelectionRowItemYear(
                isSelected = isSelected,
                index = index,
                selection = selection,
                onFilterDateSelectionChanged = onFilterDateSelectionChanged
            )
        }

    }
}

@Composable
private fun DateSelectionRowItemAll(
    isSelected: Boolean,
    index: Int,
    onFilterDateSelectionChanged: (FilterDateSelectionChanged) -> Unit
) {
    RadioButtonContentRow(
        isSelected = isSelected,
        text = stringResource(R.string.edit_art_filters_date_include_all)
    ) {
        onFilterDateSelectionChanged(
            FilterDateSelectionChanged(
                index
            )
        )
    }
}

@Composable
private fun DateSelectionRowItemCustom(
    isSelected: Boolean,
    index: Int,
    selection: DateSelection.Custom,
    onFilterDateSelectionChanged: (FilterDateSelectionChanged) -> Unit,
    onFilterAfterChanged: (FilterDateCustomChanged.FilterAfterChanged) -> Unit,
    onFilterBeforeChanged: (FilterDateCustomChanged.FilterBeforeChanged) -> Unit
) {
    val rangeSelected = selection.dateSelected
    val rangeTotal = selection.dateTotal

    val ymdTotalStart = YearMonthDay.fromUnixMs(rangeTotal.first)
    val ymdTotalEnd = YearMonthDay.fromUnixMs(rangeTotal.last)

    val ymdSelectedStart = rangeSelected.run { YearMonthDay.fromUnixMs(first) }
    val ymdSelectedEnd = rangeSelected.run { YearMonthDay.fromUnixMs(last) }

    val context = LocalContext.current

    val startDialog = ymdSelectedStart.run {
        DatePickerDialog(LocalContext.current).apply {
            datePicker.updateDate(year, month, day)
            // Force cancel to reset active date
            setOnDismissListener { datePicker.updateDate(year, month, day) }
            setOnDateSetListener { _, year, month, dayOfMonth ->
                onFilterAfterChanged(
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
                onFilterBeforeChanged(
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
    RadioButtonContentRow(
        isSelected = isSelected,
        text = stringResource(R.string.edit_art_filters_date_custom_range),
        content = {
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
    ) {
        onFilterDateSelectionChanged(
            FilterDateSelectionChanged(
                index
            )
        )
    }
}

@Composable
private fun DateSelectionRowItemYear(
    isSelected: Boolean,
    index: Int,
    selection: DateSelection.Year,
    onFilterDateSelectionChanged: (FilterDateSelectionChanged) -> Unit
) {
    RadioButtonContentRow(
        isSelected = isSelected,
        text = selection.year.toString()
    ) {
        onFilterDateSelectionChanged(
            FilterDateSelectionChanged(
                index
            )
        )
    }
}