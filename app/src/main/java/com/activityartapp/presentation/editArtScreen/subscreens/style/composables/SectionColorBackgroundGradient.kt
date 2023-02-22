package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleBackgroundColorCountChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorPendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewState
import com.activityartapp.presentation.editArtScreen.StyleIdentifier
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.BackgroundType

@Composable
fun SectionColorBackgroundGradient(
    colorList: List<ColorWrapper>,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorCountChanged: (StyleBackgroundColorCountChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    Section(
        header = stringResource(R.string.edit_art_style_background_gradient_header),
        description = stringResource(R.string.edit_art_style_background_gradient_description)
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            if (colorList.size > EditArtViewState.MIN_GRADIENT_BG_COLORS) {
                Button(
                    emphasis = ButtonEmphasis.MEDIUM,
                    size = ButtonSize.MEDIUM,
                    text = "Remove This Color"
                ) {
                    onColorCountChanged(
                        StyleBackgroundColorCountChanged(
                            changedTo = colorList.size - 1
                        )
                    )
                }
            }
            if (colorList.size < EditArtViewState.MAX_GRADIENT_BG_COLORS) {
                Button(
                    emphasis = ButtonEmphasis.HIGH,
                    size = ButtonSize.MEDIUM,
                    text = "Add a New Color to the Gradient"
                ) {
                    onColorCountChanged(
                        StyleBackgroundColorCountChanged(
                            changedTo = colorList.size + 1
                        )
                    )
                }
            }
        }
    }
}

