package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.runtime.Composable
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
        (ColorSliderRow(
            colorName = "Red",
            colorValue = color.red,
            colorType = RED,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
        (ColorSliderRow(
            colorName = "Green",
            colorValue = color.green,
            colorType = GREEN,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
        (ColorSliderRow(
            colorName = "Blue",
            colorValue = color.blue,
            colorType = BLUE,
            styleType = styleType,
            eventReceiver = eventReceiver
        ))
    }
}