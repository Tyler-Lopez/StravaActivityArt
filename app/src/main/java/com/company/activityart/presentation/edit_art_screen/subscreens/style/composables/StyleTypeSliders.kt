package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.ColorType.*
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.StyleType
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section

@Composable
fun StyleTypeSliders(
    header: String,
    description: String,
    styleType: StyleType,
    color: ColorWrapper,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(header = header, description = description) {
        ColorPreview(colorWrapper = color)
        (ColorSlider(
            colorName = stringResource(R.string.edit_art_style_color_red, color.redAsEightBit),
            colorValue = color.red,
            colorType = RED,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
        (ColorSlider(
            colorName = stringResource(R.string.edit_art_style_color_green, color.greenAsEightBit),
            colorValue = color.green,
            colorType = GREEN,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
        (ColorSlider(
            colorName = stringResource(R.string.edit_art_style_color_blue, color.blueAsEightBit),
            colorValue = color.blue,
            colorType = BLUE,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
    }
}