package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.DateSelection
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.*
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.Top
            ) {
                var heightRadioButton by remember { mutableStateOf(0.dp) }
                val localDensity = LocalDensity.current
                RadioButton(
                    selected = index == dateSelectionSelectedIndex,
                    onClick = {
                        eventReceiver.onEvent(
                            EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDateSelectionChanged(
                                index
                            )
                        )
                    },
                    modifier = Modifier
                        .onGloballyPositioned {
                            heightRadioButton = localDensity.run { it.size.height.toDp() }
                        }
                )
                println("Here, height radio button is $heightRadioButton")
                Column(
                    modifier = Modifier.defaultMinSize(minHeight = heightRadioButton),
                    verticalArrangement = Arrangement.spacedBy(
                        spacing.medium,
                        Alignment.CenterVertically
                    )
                ) {
                    when (selection) {
                        is DateSelection.All -> Subhead(text = stringResource(R.string.edit_art_filters_date_include_all))
                        is DateSelection.Year -> Subhead(text = "${selection.year}")
                        is DateSelection.Custom -> {
                            val enabled = index == dateSelectionSelectedIndex

                            Subhead(text = stringResource(R.string.edit_art_filters_date_custom_range))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                selection.apply {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(spacing.small)
                                    ) {
                                        SubheadHeavy(text = stringResource(R.string.edit_art_filters_date_start))
                                        MediumEmphasisButton(
                                            size = ButtonSize.LARGE,
                                            enabled = enabled,
                                            text = "$ymdSelectedStart"
                                        ) {
                                            startDialog.show()
                                        }
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(spacing.small)
                                    ) {
                                        SubheadHeavy(text = stringResource(R.string.edit_art_filters_date_end))
                                        MediumEmphasisButton(
                                            size = ButtonSize.LARGE,
                                            enabled = enabled,
                                            text = "$ymdSelectedEnd"
                                        ) {
                                            endDialog.show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}