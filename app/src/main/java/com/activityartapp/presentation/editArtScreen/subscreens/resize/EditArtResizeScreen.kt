package com.activityartapp.presentation.editArtScreen.subscreens.resize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.Resolution
import com.activityartapp.presentation.editArtScreen.Resolution.RotatingResolution
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliderSpecification
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliders
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun EditArtResizeScreen(
    resolutionList: SnapshotStateList<Resolution>,
    selectedResolutionIndex: State<Int>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_resize_header),
        description = stringResource(R.string.edit_art_resize_description),
    ) {
        ResolutionLazyList(
            selectedResolutionIndex = selectedResolutionIndex.value,
            resolutionList = resolutionList,
            onSizeRotated = eventReceiver::onEvent,
            onSizeChanged = eventReceiver::onEvent,
            onSizeCustomChanged = eventReceiver::onEvent,
            onSizeCustomPendingChangeConfirmed = eventReceiver::onEvent,
            onSizeCustomPendingHeightChanged = eventReceiver::onEvent,
            onSizeCustomPendingWidthChanged = eventReceiver::onEvent
        )
    }
}

@Composable
private fun ResolutionLazyList(
    selectedResolutionIndex: Int,
    resolutionList: List<Resolution>,
    onSizeRotated: (SizeRotated) -> Unit,
    onSizeCustomPendingChangeConfirmed: (SizeCustomPendingChangeConfirmed) -> Unit,
    onSizeCustomChanged: (SizeCustomChanged) -> Unit,
    onSizeCustomPendingWidthChanged: (EditArtViewEvent.SizeCustomPendingChanged.WidthChanged) -> Unit,
    onSizeCustomPendingHeightChanged: (EditArtViewEvent.SizeCustomPendingChanged.HeightChanged) -> Unit,
    onSizeChanged: (SizeChanged) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        itemsIndexed(resolutionList) { index, resolution ->
                ListItem(
                    isSelected = selectedResolutionIndex == index,
                    index = index,
                    res = resolution,
                    onSizeRotated,
                    onSizeCustomPendingChangeConfirmed,
                    onSizeCustomChanged,
                    onSizeCustomPendingWidthChanged,
                    onSizeCustomPendingHeightChanged,
                    onSizeChanged
                )
            }
    }

}

@Composable
private fun ListItem(
    isSelected: Boolean,
    index: Int,
    res: Resolution,
    onSizeRotated: (SizeRotated) -> Unit,
    onSizeCustomPendingChangeConfirmed: (SizeCustomPendingChangeConfirmed) -> Unit,
    onSizeCustomChanged: (SizeCustomChanged) -> Unit,
    onSizeCustomPendingWidthChanged: (EditArtViewEvent.SizeCustomPendingChanged.WidthChanged) -> Unit,
    onSizeCustomPendingHeightChanged: (EditArtViewEvent.SizeCustomPendingChanged.HeightChanged) -> Unit,
    onSizeChanged: (SizeChanged) -> Unit
) {
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
            content = (res as? Resolution.CustomResolution)?.let {
                {
                    val range = it.sizeRangePx
                    val onTextFieldDone = {
                        println("here, on text field done...")
                        onSizeCustomPendingChangeConfirmed(
                            SizeCustomPendingChangeConfirmed(
                                customIndex = index
                            )
                        )
                    }
                    TextFieldSliders(
                        specifications = listOf(
                            TextFieldSliderSpecification(
                                pendingChangesMessage = it.pendingWidth?.let {
                                    stringResource(R.string.edit_art_resize_custom_pending_change_prompt)
                                },
                                keyboardType = KeyboardType.Number,
                                textFieldLabel = stringResource(R.string.edit_art_resize_pixels_width),
                                textFieldValue = it.pendingWidth ?: "%.0f".format(it.sizeWidthPx),
                                sliderValue = it.sizeWidthPx,
                                sliderRange = range,
                                onSliderChanged = {
                                    onSizeCustomChanged(
                                        SizeCustomChanged(
                                            index = index,
                                            changedTo = it,
                                            heightChanged = false
                                        )
                                    )
                                },
                                onTextFieldChanged = { str ->
                                    onSizeCustomPendingWidthChanged(
                                        EditArtViewEvent.SizeCustomPendingChanged.WidthChanged(
                                            changedTo = str
                                        )
                                    )
                                },
                                onTextFieldDone = onTextFieldDone
                            ),
                            TextFieldSliderSpecification(
                                pendingChangesMessage = it.pendingHeight?.let {
                                    stringResource(R.string.edit_art_resize_custom_pending_change_prompt)
                                },
                                keyboardType = KeyboardType.Number,
                                textFieldLabel = stringResource(R.string.edit_art_resize_pixels_height),
                                textFieldValue = it.pendingHeight ?: "%.0f".format(it.sizeHeightPx),
                                sliderValue = it.sizeHeightPx,
                                sliderRange = range,
                                onSliderChanged = {
                                    onSizeCustomChanged(
                                        SizeCustomChanged(
                                            index = index,
                                            changedTo = it,
                                            heightChanged = true
                                        )
                                    )
                                },
                                onTextFieldChanged = { str ->
                                    onSizeCustomPendingHeightChanged(
                                        EditArtViewEvent.SizeCustomPendingChanged.HeightChanged(
                                            changedTo = str
                                        )
                                    )
                                },
                                onTextFieldDone = onTextFieldDone
                            )
                        )
                    )
                }
            },
            onActionButtonPressed = {
                onSizeRotated(SizeRotated(rotatedIndex = index))
            },
            isSelected = isSelected,
            text = res.displayTextResolution(),
            subtext = (res as? RotatingResolution)?.displayTextPixels()
        ) {
            onSizeChanged(SizeChanged(changedIndex = index))
        }
    }
}
