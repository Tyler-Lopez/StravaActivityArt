package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Slider
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.ColorType.*
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.StylesColorChanged
import com.company.activityart.presentation.edit_art_screen.StyleType
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

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