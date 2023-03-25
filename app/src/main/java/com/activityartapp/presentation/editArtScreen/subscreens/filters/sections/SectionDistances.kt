package com.activityartapp.presentation.editArtScreen.subscreens.filters.sections

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDistanceChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterDistancePendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.FilterDistancePendingChange
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterCount
import com.activityartapp.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SectionDistances(
    count: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distancePendingChangeStart: String?,
    distancePendingChangeEnd: String?,
    distanceTotal: ClosedFloatingPointRange<Double>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val focus = LocalFocusManager.current
    val adjDistanceSelected = distanceSelected?.run {
        start.toFloat()..endInclusive.toFloat()
    }
    val adjDistanceTotal = distanceTotal.start.toFloat()..distanceTotal.endInclusive.toFloat()


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        val range = (distanceSelected ?: distanceTotal)
        DistanceTextField(
            textFieldValue = distancePendingChangeStart
                ?: "%.2f".format(range.start.meterToMiles()),
            labelString = stringResource(R.string.edit_art_filters_distance_label_shortest),
            onDone = {
                eventReceiver.onEvent(
                    event = FilterDistancePendingChangeConfirmed.StartConfirmed
                )
                focus.clearFocus()
            },
            onValueChanged = { str ->
                eventReceiver.onEvent(
                    event = FilterDistancePendingChange.FilterDistancePendingChangeShortest(
                        changedTo = str
                    )
                )
            }
        )
        DistanceTextField(
            textFieldValue = distancePendingChangeEnd
                ?: "%.2f".format(range.endInclusive.meterToMiles()),
            labelString = stringResource(R.string.edit_art_filters_distance_label_longest),
            onDone = {
                eventReceiver.onEvent(
                    event = FilterDistancePendingChangeConfirmed.EndConfirmed
                )
                focus.clearFocus()
            },
            onValueChanged = { str ->
                eventReceiver.onEvent(
                    event = FilterDistancePendingChange.FilterDistancePendingChangeLongest(
                        changedTo = str
                    )
                )
            }
        )
    }
    if (distancePendingChangeStart != null || distancePendingChangeEnd != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.small, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.edit_art_filters_distance_pending_change_prompt),
                modifier = Modifier.weight(1f, false),
                style = MaterialTheme.typography.caption
            )
            com.activityartapp.presentation.common.button.Button(
                emphasis = ButtonEmphasis.MEDIUM,
                size = ButtonSize.SMALL,
                text = stringResource(R.string.edit_art_filters_distance_pending_change_prompt_accept),
            ) {
                listOf(
                    FilterDistancePendingChangeConfirmed.StartConfirmed,
                    FilterDistancePendingChangeConfirmed.EndConfirmed
                ).forEach { eventReceiver.onEvent(it) }
            }
        }
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
    FilterCount(count)
}

@Composable
private fun RowScope.DistanceTextField(
    textFieldValue: String,
    labelString: String,
    onDone: () -> Unit,
    onValueChanged: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.weight(weight = 1f, fill = true),
        keyboardActions = KeyboardActions(onDone = {
            onDone()
            focusManager.clearFocus()
        }),
        label = {
            Text(
                text = labelString,
                style = MaterialTheme.typography.overline
            )
        },
        singleLine = true,
        interactionSource = interactionSource,
    )
}

private fun Double.meterToMiles(): Double = this * 0.000621371192