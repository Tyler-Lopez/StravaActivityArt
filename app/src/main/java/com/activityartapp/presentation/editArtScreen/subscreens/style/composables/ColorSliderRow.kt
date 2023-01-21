package com.activityartapp.activityart.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.StyleType
import com.activityartapp.presentation.ui.theme.spacing

// Todo, rework this, very sloppy atm
@Composable
fun ColorSlider(
    colorName: String,
    colorValue: Float,
    colorType: ColorType,
    enabled: Boolean,
    styleType: StyleType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        SubheadHeavy(colorName, modifier = Modifier.padding(start = spacing.small))
        Slider(
            modifier = Modifier.weight(1f, true),
            value = colorValue,
            enabled = enabled,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = {
                eventReceiver.onEvent(
                    StylesColorChanged(
                        styleType = styleType,
                        colorType = colorType,
                        changedTo = it
                    )
                )
            }
        )
    }
}