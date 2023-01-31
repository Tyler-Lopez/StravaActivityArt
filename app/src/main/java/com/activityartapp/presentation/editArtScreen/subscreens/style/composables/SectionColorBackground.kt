package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorsBackgroundChanged
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.util.enums.BackgroundType

@Composable
fun ColumnScope.SectionColorBackground(
    backgroundType: BackgroundType,
    colors: List<ColorWrapper>,
    onColorChanged: (StyleColorsBackgroundChanged) -> Unit
) {
    if (backgroundType != BackgroundType.TRANSPARENT) {
        Section(
            header = stringResource(R.string.edit_art_style_background_header),
            description = stringResource(R.string.edit_art_style_background_type_solid_description)
        ) {
            colors.forEachIndexed { index, color ->
                ColorPreview(colorWrapper = color)
                ColorSlidersRGB(
                    color = color,
                    enabled = true,
                    onColorChanged = {
                        onColorChanged(
                            StyleColorsBackgroundChanged(
                                changedIndex = index,
                                changedColorType = it.first,
                                changedTo = it.second
                            )
                        )
                    }
                )
            }
        }
    }
}