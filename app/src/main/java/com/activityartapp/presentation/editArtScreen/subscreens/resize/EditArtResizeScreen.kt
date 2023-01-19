package com.activityartapp.presentation.editArtScreen.subscreens.resize

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.SizeCustomChanged
import com.activityartapp.presentation.editArtScreen.Resolution
import com.activityartapp.presentation.editArtScreen.Resolution.CustomResolution
import com.activityartapp.presentation.editArtScreen.Resolution.RotatingResolution
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.ext.toFloatRange
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.editArtScreen.composables.Section
import kotlin.math.roundToInt

@Composable
fun ColumnScope.EditArtResizeScreen(
    // customHeightPx: Int,
    //  customWidthPx: Int,
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
                RadioButton(
                    selected = isSelected,
                    onClick = { eventReceiver.onEvent(SizeChanged(index)) }
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                        Body(text = res.displayTextResolution())
                        when (res) {
                            is CustomResolution -> {
                                SubheadHeavy(
                                    text = stringResource(
                                        R.string.edit_art_resize_pixels_width,
                                        //customWidthPx
                                        res.widthPx
                                    ),
                                    modifier = Modifier.padding(start = spacing.small)
                                )
                                CustomDimensionRangeSlider(
                                    eventReceiver = eventReceiver,
                                    isEnabled = isSelected,
                                    isWidth = true,
                                    value = res.widthPx,
                                    valueRange = customRangePx
                                )
                                SubheadHeavy(
                                    text = stringResource(
                                        R.string.edit_art_resize_pixels_height, res.heightPx
                                    ),
                                    modifier = Modifier.padding(start = spacing.small)
                                )
                                CustomDimensionRangeSlider(
                                    isEnabled = isSelected,
                                    isWidth = false,
                                    eventReceiver = eventReceiver,
                                    value = res.heightPx,
                                    valueRange = customRangePx
                                )
                            }
                            is RotatingResolution -> SubheadHeavy(text = res.displayTextPixels())
                        }
                    }

                    if (res is RotatingResolution && res.swappingChangesSize) {
                        MediumEmphasisButton(
                            imageVector = Icons.Default.RotateRight,
                            modifier = Modifier,
                            size = ButtonSize.MEDIUM,
                            onClick = {
                                eventReceiver.onEvent(
                                    SizeRotated(
                                        index
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomDimensionRangeSlider(
    isEnabled: Boolean,
    isWidth: Boolean,
    value: Int,
    valueRange: IntRange,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Slider(
        enabled = isEnabled,
        value = value.toFloat(),
        valueRange = valueRange.toFloatRange(),
        onValueChange = {
            eventReceiver.onEvent(
                if (isWidth) {
                    SizeCustomChanged.WidthChanged(it.roundToInt())
                } else {
                    SizeCustomChanged.HeightChanged(it.roundToInt())
                }
            )
        },
        onValueChangeFinished = { eventReceiver.onEvent(SizeCustomChangeDone) }
    )
}