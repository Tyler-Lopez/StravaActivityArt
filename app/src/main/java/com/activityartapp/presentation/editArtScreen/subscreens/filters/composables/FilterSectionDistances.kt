package com.activityartapp.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.textField.MediumEmphasisTextField
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.FilterDistancePendingChange
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.*
import com.activityartapp.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.FilterSectionDistances(
    count: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distancePendingChangeStart: String?,
    distancePendingChangeEnd: String?,
    distanceTotal: ClosedFloatingPointRange<Double>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val adjDistanceSelected = distanceSelected?.run {
        start.toFloat()..endInclusive.toFloat()
    }
    val adjDistanceTotal = distanceTotal.start.toFloat()..distanceTotal.endInclusive.toFloat()

    FilterSection(
        count = count,
        header = stringResource(R.string.edit_art_filters_distance_header),
        description = stringResource(R.string.edit_art_filters_distance_description),
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val range = (distanceSelected ?: distanceTotal)
            OutlinedTextField(
                value = distancePendingChangeStart ?: "%.2f".format(range.start.meterToMiles()),
                onValueChange = { str ->
                    eventReceiver.onEvent(
                        event = FilterDistancePendingChange.FilterDistancePendingChangeShortest(
                            changedTo = str
                        )
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        eventReceiver.onEvent(
                            event = FilterDistancePendingChangeConfirmed.StartConfirmed
                        )
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                label = {
                    Text(
                        text = "SHORTEST",
                        style = MaterialTheme.typography.overline
                    )
                },
                modifier = Modifier
                    .weight(1f, false),
                trailingIcon = {
                    Text(
                        text = "MI",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            )
            OutlinedTextField(
                value = distancePendingChangeEnd ?: "%.2f".format(range.endInclusive.meterToMiles()),
                onValueChange = { str ->
                    eventReceiver.onEvent(
                        event = FilterDistancePendingChange.FilterDistancePendingChangeLongest(
                            changedTo = str
                        )
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        eventReceiver.onEvent(
                            event = FilterDistancePendingChangeConfirmed.EndConfirmed
                        )
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                label = {
                    Text(
                        text = "LONGEST",
                        style = MaterialTheme.typography.overline
                    )
                },
                modifier = Modifier
                    .weight(1f, false),
                trailingIcon = {
                    Text(
                        text = "MI",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            )
            /*
            OutlinedTextField(
                value = "%.2f".format(it.endInclusive.meterToMiles()),
                modifier = Modifier.weight(1f, false),
                onValueChange = { str ->
                    eventReceiver.onEvent(
                        EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDistanceChanged(
                            it.run { start..str.toDouble().coerceAtLeast(start) }
                        )
                    )
                },
                label = {
                    Text(
                        text = "LONGEST",
                        style = MaterialTheme.typography.overline
                    )
                },
                trailingIcon = {
                    Text(
                        text = "MI",
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            )

             */
            /*
            MediumEmphasisTextField(
                modifier = Modifier.weight(1f, false),
                text = "%.2f".format(it.start.meterToMiles()) + " mi",
                label = "Shortest"
            ) {

            }
            MediumEmphasisTextField(
                modifier = Modifier.weight(1f, false),
                text = "%.2f".format(it.endInclusive.meterToMiles()) + " mi",
                label = "Longest"
            ) {

            }

             */
        }
        RangeSlider(
            value = adjDistanceSelected ?: adjDistanceTotal,
            onValueChange = {
                eventReceiver.onEvent(
                    event = FilterDistanceChanged(it.run { start.toDouble()..endInclusive.toDouble() })
                )
            },
            valueRange = adjDistanceTotal
        )
    }
}

private fun Double.meterToMiles(): Double = this * 0.000621371192
private fun Double.milesToMeters(): Double = this / 0.000621371192
