package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.runtime.Composable
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorPendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.StyleIdentifier
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorPreview
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorSlidersRGB

@Composable
fun SectionColorBackgroundSolid(
    color: ColorWrapper,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    ColorPreview(colorWrapper = color)
    ColorSlidersRGB(
        color = color,
        onColorChanged = { colorType, changedTo ->
            onColorChanged(
                StyleColorChanged(
                    style = StyleIdentifier.Background(index = 0),
                    colorType = colorType,
                    changedTo = changedTo
                )
            )
        },
        onColorPendingChanged = { colorType, changedTo ->
            onColorPendingChanged(
                StyleColorPendingChanged(
                    style = StyleIdentifier.Background(index = 0),
                    colorType = colorType,
                    changedTo = changedTo
                )
            )
        },
        onColorPendingChangeConfirmed = {
            onColorPendingChangeConfirmed(
                StyleColorPendingChangeConfirmed(
                    style = StyleIdentifier.Background(index = 0),
                )
            )
        }
    )
}

