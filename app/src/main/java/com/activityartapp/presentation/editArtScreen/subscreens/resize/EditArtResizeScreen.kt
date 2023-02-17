package com.activityartapp.presentation.editArtScreen.subscreens.resize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.Resolution
import com.activityartapp.presentation.editArtScreen.Resolution.RotatingResolution
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliderSpecification
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliders
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.ext.toFloatRange
import kotlin.math.roundToInt

@Composable
fun EditArtResizeScreen(
    customRangePx: IntRange,
    resolutionList: List<Resolution>,
    selectedResolutionIndex: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
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
                                TextFieldSliders(
                                    specifications = listOf(
                                        TextFieldSliderSpecification(
                                            pendingChangesMessage = it.pendingWidth?.let {
                                                stringResource(R.string.edit_art_resize_custom_pending_change_prompt)
                                            },
                                            keyboardType = KeyboardType.Number,
                                            textFieldLabel = stringResource(R.string.edit_art_resize_pixels_width),
                                            textFieldValue = it.pendingWidth
                                                ?: it.widthPx.toString(),
                                            sliderValue = it.widthPx.toFloat(),
                                            sliderRange = customRangePx.toFloatRange(),
                                            onSliderChanged = {
                                                eventReceiver.onEvent(
                                                    SizeCustomChanged.WidthChanged(
                                                        it.toInt()
                                                    )
                                                )
                                            },
                                            onTextFieldChanged = { str ->
                                                eventReceiver.onEvent(
                                                    EditArtViewEvent.SizeCustomPendingChanged.WidthChanged(
                                                        changedTo = str
                                                    )
                                                )
                                            },
                                            onTextFieldDone = {
                                                eventReceiver.onEvent(
                                                    SizeCustomPendingChangeConfirmed
                                                )
                                            }
                                        ),
                                        TextFieldSliderSpecification(
                                            pendingChangesMessage = it.pendingHeight?.let {
                                                stringResource(R.string.edit_art_resize_custom_pending_change_prompt)
                                            },
                                            keyboardType = KeyboardType.Number,
                                            textFieldLabel = stringResource(R.string.edit_art_resize_pixels_height),
                                            textFieldValue = it.pendingHeight
                                                ?: it.heightPx.toString(),
                                            sliderValue = it.heightPx.toFloat(),
                                            sliderRange = customRangePx.toFloatRange(),
                                            onSliderChanged = {
                                                eventReceiver.onEvent(
                                                    SizeCustomChanged.HeightChanged(
                                                        it.toInt()
                                                    )
                                                )
                                            },
                                            onTextFieldChanged = { str ->
                                                eventReceiver.onEvent(
                                                    EditArtViewEvent.SizeCustomPendingChanged.HeightChanged(
                                                        changedTo = str
                                                    )
                                                )
                                            },
                                            onTextFieldDone = {
                                                eventReceiver.onEvent(
                                                    SizeCustomPendingChangeConfirmed
                                                )
                                            }
                                        )
                                    )
                                )
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
