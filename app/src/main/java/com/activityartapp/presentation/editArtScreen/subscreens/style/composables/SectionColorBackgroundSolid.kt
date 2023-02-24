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
fun SectionColorBackgroundSolid(
    color: ColorWrapper,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    Section(
        header = stringResource(R.string.edit_art_style_background_solid_header),
        description = stringResource(R.string.edit_art_style_background_solid_description)
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
}

