package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorPendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.StyleIdentifier
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.util.enums.BackgroundType

@Composable
fun SectionColorBackground(
    backgroundType: BackgroundType,
    colorList: List<ColorWrapper>,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    backgroundType.backgroundColorStrRes?.let {
        Section(
            header = stringResource(it.first),
            description = stringResource(it.second)
        ) {
            colorList.forEachIndexed { index, color ->
                ColorPreview(colorWrapper = color)
                ColorSlidersRGB(
                    color = color,
                    onColorChanged = { colorType, changedTo ->
                        onColorChanged(
                            StyleColorChanged(
                                style = StyleIdentifier.Background(index = index),
                                colorType = colorType,
                                changedTo = changedTo
                            )
                        )
                    },
                    onColorPendingChanged = { colorType, changedTo ->
                        onColorPendingChanged(
                            StyleColorPendingChanged(
                                style = StyleIdentifier.Background(index = index),
                                colorType = colorType,
                                changedTo = changedTo
                            )
                        )
                    },
                    onColorPendingChangeConfirmed = {
                        onColorPendingChangeConfirmed(
                            StyleColorPendingChangeConfirmed(
                                style = StyleIdentifier.Background(index = index),
                            )
                        )
                    }
                )
            }
        }
    }
}

