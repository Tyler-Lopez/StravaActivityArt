package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorPendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.StyleType
import com.activityartapp.presentation.editArtScreen.composables.Section

@Composable
fun SectionColorActivities(
    color: ColorWrapper,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    Section(
        header = stringResource(R.string.edit_art_style_activities_header),
        description = stringResource(R.string.edit_art_style_activities_description)
    ) {
        ColorPreview(colorWrapper = color)
        ColorSlidersRGB(
            color = color,
            onColorChanged = { colorType, changedTo ->
                onColorChanged(
                    StyleColorChanged(
                        styleType = StyleType.ACTIVITIES,
                        colorType = colorType,
                        changedTo = changedTo
                    )
                )
            },
            onColorPendingChanged = { colorType, changedTo ->
                onColorPendingChanged(
                    StyleColorPendingChanged(
                        styleType = StyleType.ACTIVITIES,
                        colorType = colorType,
                        changedTo = changedTo
                    )
                )
            },
            onColorPendingChangeConfirmed = {
                onColorPendingChangeConfirmed(StyleColorPendingChangeConfirmed(StyleType.ACTIVITIES))
            }
        )
    }
}