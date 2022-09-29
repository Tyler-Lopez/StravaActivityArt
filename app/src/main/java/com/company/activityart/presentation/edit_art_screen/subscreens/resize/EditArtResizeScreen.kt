package com.company.activityart.presentation.edit_art_screen.subscreens.resize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.SizeRotated
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.SizeChanged
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.SizeCustomChangeDone
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.SizeCustomChanged
import com.company.activityart.presentation.edit_art_screen.Resolution
import com.company.activityart.presentation.edit_art_screen.Resolution.CustomResolution
import com.company.activityart.presentation.edit_art_screen.Resolution.SwappableResolution
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.ext.toFloatRange
import kotlin.math.roundToInt

@Composable
fun EditArtResizeScreen(
    customHeightPx: Int,
    customWidthPx: Int,
    customRangePx: IntRange,
    resolutionList: List<Resolution>,
    selectedResolutionIndex: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
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
                                        stringResource(
                                            R.string.edit_art_resize_pixels_width, customWidthPx
                                        )
                                    )
                                    CustomDimensionRangeSlider(
                                        eventReceiver = eventReceiver,
                                        isEnabled = isSelected,
                                        isWidth = true,
                                        value = customWidthPx,
                                        valueRange = customRangePx
                                    )
                                    SubheadHeavy(
                                        stringResource(
                                            R.string.edit_art_resize_pixels_height, customHeightPx
                                        )
                                    )
                                    CustomDimensionRangeSlider(
                                        isEnabled = isSelected,
                                        isWidth = false,
                                        eventReceiver = eventReceiver,
                                        value = customHeightPx,
                                        valueRange = customRangePx
                                    )
                                }
                                is SwappableResolution -> SubheadHeavy(text = res.displayTextPixels())
                            }
                        }

                        if (res is SwappableResolution && res.swappingChangesSize) {
                            MediumEmphasisButton(
                                imageVector = Icons.Default.RotateRight,
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
        onValueChangeFinished = {
            eventReceiver.onEvent(
                if (isWidth) {
                    SizeCustomChangeDone.WidthChanged
                } else {
                    SizeCustomChangeDone.HeightChanged
                }
            )
        }
    )
}