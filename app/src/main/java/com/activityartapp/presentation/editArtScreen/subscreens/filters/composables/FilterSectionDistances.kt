package com.activityartapp.presentation.editArtScreen.subscreens.filters.composables

import android.util.MutableInt
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
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
    val focus = LocalFocusManager.current
    val adjDistanceSelected = distanceSelected?.run {
        start.toFloat()..endInclusive.toFloat()
    }
    val adjDistanceTotal = distanceTotal.start.toFloat()..distanceTotal.endInclusive.toFloat()

    FilterSection(
        count = count,
        header = stringResource(R.string.edit_art_filters_distance_header),
        description = stringResource(R.string.edit_art_filters_distance_description),
    ) {

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
                labelString = stringResource(R.string.edit_art_filters_distance_label_shortest),
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
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RowScope.DistanceTextField(
    textFieldValue: String,
    labelString: String,
    onDone: () -> Unit,
    onValueChanged: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.weight(1f, true),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        interactionSource = interactionSource,
        decorationBox = {
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = textFieldValue,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                label = {
                    Text(
                        text = labelString,
                        style = MaterialTheme.typography.overline
                    )
                },
                interactionSource = interactionSource,
            )
        }
    )
}

private fun Double.meterToMiles(): Double = this * 0.000621371192