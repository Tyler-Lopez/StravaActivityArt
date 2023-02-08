package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorFontChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorFontUseCustomChanged
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section

@Composable
fun ColumnScope.SectionColorText(
    color: ColorWrapper?,
    colorActivities: ColorWrapper,
    onColorChanged: (StyleColorFontChanged) -> Unit,
    onUseFontChanged: (StyleColorFontUseCustomChanged) -> Unit
) {
    val customEnabled = color != null
    Section(header = stringResource(R.string.edit_art_style_text_header)) {
        ColorPreview(colorWrapper = color ?: colorActivities)
        ColumnSmallSpacing {
            RadioButtonContentRow(
                isSelected = !customEnabled,
                text = stringResource(R.string.edit_art_style_font_use_activities)
            ) { onUseFontChanged(StyleColorFontUseCustomChanged(useCustom = false)) }
            RadioButtonContentRow(
                isSelected = customEnabled,
                text = stringResource(R.string.edit_art_style_font_use_custom),
                content = {
                    ColorSlidersRGB(
                        color = color ?: colorActivities,
                        enabled = customEnabled,
                        onColorChanged = {
                            onColorChanged(
                                StyleColorFontChanged(
                                    colorType = it.first,
                                    changedTo = it.second
                                )
                            )
                        }
                    )
                }) { onUseFontChanged(StyleColorFontUseCustomChanged(useCustom = true)) }
        }
    }
}