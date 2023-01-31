package com.activityartapp.presentation.editArtScreen.subscreens.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorSlider
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleBackgroundTypeChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StylesStrokeWidthChanged
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.presentation.editArtScreen.*
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonWithText
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorPreview
import com.activityartapp.util.enums.BackgroundType

@Composable
fun ColumnScope.EditArtStyleViewDelegate(
    backgroundColors: List<ColorWrapper>,
    backgroundType: BackgroundType,
    colorActivities: ColorWrapper,
    colorFont: ColorWrapper?,
    strokeWidthType: StrokeWidthType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_style_background_type_header),
        description = stringResource(R.string.edit_art_style_background_type_description)
    ) {
        BackgroundType.values().forEach {
            RadioButtonWithText(
                isSelected = it == backgroundType,
                text = stringResource(it.strRes)
            ) { eventReceiver.onEvent(StyleBackgroundTypeChanged(changedTo = it)) }
        }
    }
    StyleType.values().forEach { styleType ->
        if (styleType == StyleType.BACKGROUND && backgroundType == BackgroundType.TRANSPARENT) {
            // no-op
        } else {
            Section(
                header = stringResource(styleType.headerStrRes),
                description = stringResource(styleType.descriptionStrRes)
            ) {
                val color: ColorWrapper = when (styleType) {
                    StyleType.ACTIVITIES -> colorActivities
                    StyleType.BACKGROUND -> backgroundColors.first()
                    StyleType.FONT -> colorFont ?: colorActivities
                }
                ColorPreview(colorWrapper = color)
                if (styleType == StyleType.FONT) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = colorFont == null,
                            onClick = {
                                eventReceiver.onEvent(
                                    EditArtViewEvent.ArtMutatingEvent.StyleColorFontUseCustomChanged(
                                        false
                                    )
                                )
                            }
                        )
                        Subhead(text = "Use the same color as Activities")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                    verticalAlignment = Alignment.Top
                ) {
                    if (styleType == StyleType.FONT) {
                        RadioButton(
                            selected = colorFont != null,
                            onClick = {
                                eventReceiver.onEvent(
                                    EditArtViewEvent.ArtMutatingEvent.StyleColorFontUseCustomChanged(
                                        true
                                    )
                                )
                            }
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (styleType == StyleType.FONT) {
                            Subhead(text = "Choose a different color")
                        }
                        (ColorSlider(
                            colorName = stringResource(
                                R.string.edit_art_style_color_red,
                                color.redAsEightBit
                            ),
                            enabled = styleType != StyleType.FONT || colorFont != null,
                            colorValue = color.red,
                            colorType = ColorType.RED,
                            styleType = styleType,
                            eventReceiver = eventReceiver
                        ))
                        (ColorSlider(
                            colorName = stringResource(
                                R.string.edit_art_style_color_green,
                                color.greenAsEightBit
                            ),
                            enabled = styleType != StyleType.FONT || colorFont != null,
                            colorValue = color.green,
                            colorType = ColorType.GREEN,
                            styleType = styleType,
                            eventReceiver = eventReceiver
                        ))
                        (ColorSlider(
                            colorName = stringResource(
                                R.string.edit_art_style_color_blue,
                                color.blueAsEightBit
                            ),
                            enabled = styleType != StyleType.FONT || colorFont != null,
                            colorValue = color.blue,
                            colorType = ColorType.BLUE,
                            styleType = styleType,
                            eventReceiver = eventReceiver
                        ))
                    }
                }
            }
        }
    }
    Section(
        header = stringResource(R.string.edit_art_style_stroke_width_header),
        description = stringResource(R.string.edit_art_style_stroke_width_description)
    ) {
        StrokeWidthType.values().forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = strokeWidthType == it, onClick = {
                    eventReceiver.onEvent(
                        StylesStrokeWidthChanged(it)
                    )
                })
                Subhead(text = stringResource(id = it.headerId))
            }
        }
    }
}