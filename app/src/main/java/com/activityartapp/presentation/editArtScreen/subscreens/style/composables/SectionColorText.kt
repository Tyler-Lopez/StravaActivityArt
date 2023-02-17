package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.StyleType
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section

@Composable
fun SectionColorText(
    color: ColorWrapper?,
    colorActivities: ColorWrapper,
    onColorChanged: (StyleColorChanged) -> Unit,
    onUseFontChanged: (StyleColorFontUseCustomChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
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
                        onColorChanged = { colorType, changedTo ->
                            onColorChanged(
                                StyleColorChanged(
                                    styleType = StyleType.FONT,
                                    colorType = colorType,
                                    changedTo = changedTo
                                )
                            )
                        },
                        onColorPendingChanged = { colorType, changedTo ->
                            onColorPendingChanged(
                                StyleColorPendingChanged(
                                    styleType = StyleType.FONT,
                                    colorType = colorType,
                                    changedTo = changedTo
                                )
                            )
                        },
                        onColorPendingChangeConfirmed = {
                            onColorPendingChangeConfirmed(StyleColorPendingChangeConfirmed(StyleType.FONT))
                        }
                    )
                }) { onUseFontChanged(StyleColorFontUseCustomChanged(useCustom = true)) }
        }
    }
}