package com.activityartapp.presentation.editArtScreen.subscreens.resize

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.Resolution
import com.activityartapp.presentation.editArtScreen.Resolution.RotatingResolution
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.ext.toFloatRange
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section
import kotlin.math.roundToInt

@Composable
fun EditArtResizeScreen(
    customOutOfBoundsWidthPx: Int?,
    customOutOfBoundsHeightPx: Int?,
    customRangePx: IntRange,
    resolutionList: List<Resolution>,
    selectedResolutionIndex: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val focusManager = LocalFocusManager.current

    Section(
        header = stringResource(R.string.edit_art_resize_header),
        description = stringResource(R.string.edit_art_resize_description),
    ) {
        resolutionList.forEachIndexed { index, res ->
            val isSelected = selectedResolutionIndex == index
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(spacing.medium)
            ) {
                RadioButtonContentRow(
                    actionButtonContent = (res as? RotatingResolution)
                        ?.takeIf { it.swappingChangesSize }
                        ?.let {
                            {
                                Icon(
                                    imageVector = Icons.Default.RotateRight,
                                    contentDescription = stringResource(R.string.edit_art_resize_rotate_right_content_description)
                                )
                            }
                        },
                    content = (res as? Resolution.CustomResolution)
                        ?.let {
                            {
                                ColumnMediumSpacing {
                                    CustomDimensionController(
                                        labelText = stringResource(R.string.edit_art_resize_pixels_width),
                                        outOfBoundsPx = customOutOfBoundsWidthPx,
                                        range = customRangePx,
                                        onTextFieldDone = focusManager::clearFocus,
                                        onValueChanged = {
                                            eventReceiver.onEvent(SizeCustomChanged.WidthChanged(it))
                                        },
                                        value = it.widthPx
                                    )
                                    CustomDimensionController(
                                        labelText = stringResource(R.string.edit_art_resize_pixels_height),
                                        outOfBoundsPx = customOutOfBoundsHeightPx,
                                        range = customRangePx,
                                        onTextFieldDone = focusManager::clearFocus,
                                        onValueChanged = {
                                            eventReceiver.onEvent(SizeCustomChanged.HeightChanged(it))
                                        },
                                        value = it.heightPx
                                    )
                                }
                            }
                        },
                    onActionButtonPressed = { eventReceiver.onEvent(SizeRotated(index)) },
                    isSelected = isSelected,
                    text = res.displayTextResolution(),
                    subtext = (res as? RotatingResolution)?.displayTextPixels()
                ) { eventReceiver.onEvent(SizeChanged(index)) }
            }
        }
    }
}

@Composable
private fun CustomDimensionController(
    labelText: String,
    outOfBoundsPx: Int?,
    range: IntRange,
    onTextFieldDone: () -> Unit,
    onValueChanged: (Int) -> Unit,
    value: Int,
) {
    OutlinedTextField(
        value = (outOfBoundsPx ?: value).toString(),
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.overline
            )
        },
        onValueChange = { valueStr ->
            valueStr.toDoubleOrNull()?.roundToInt()?.let {
                onValueChanged(it)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onTextFieldDone() }
        )
    )
    outOfBoundsPx?.let {
        ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
            val errorIsTooLarge =
                it > range.last
            Text(
                text = stringResource(
                    if (errorIsTooLarge) {
                        R.string.edit_art_resize_pixels_custom_too_large_error
                    } else {
                        R.string.edit_art_resize_pixels_custom_too_small_error
                    },
                    outOfBoundsPx,
                    if (errorIsTooLarge) {
                        range.last
                    } else {
                        range.first
                    }
                ),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = stringResource(
                    id = R.string.edit_art_resize_pixels_custom_error_prompt,
                    value
                ),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
    Slider(
        value = value.toFloat(),
        valueRange = range.toFloatRange(),
        onValueChange = {
            onValueChanged(it.roundToInt())
        }
    )
}