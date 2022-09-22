package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.edit_art_screen.subscreens.style.ColorType
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyleViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyleViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.style.StyleType

@Composable
fun StyleSectionBackground(
    backgroundColor: ColorWrapper,
    eventReceiver: EventReceiver<EditArtStyleViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_style_background_header),
        description = stringResource(R.string.edit_art_style_background_description)
    ) {
        Subhead(text = stringResource(id = R.string.edit_art_style_color_red))
        ColorPreview(colorWrapper = backgroundColor)
        Slider(
            value = backgroundColor.red,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = {
                eventReceiver.onEvent(
                    ColorChanged(
                        styleType = StyleType.BACKGROUND,
                        colorType = ColorType.RED,
                        changedTo = it
                    )
                )
            }
        )
        Subhead(text = stringResource(id = R.string.edit_art_style_color_green))
        Slider(
            value = backgroundColor.green,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = {
                eventReceiver.onEvent(
                    ColorChanged(
                        styleType = StyleType.BACKGROUND,
                        colorType = ColorType.GREEN,
                        changedTo = it
                    )
                )
            }
        )
        Subhead(text = stringResource(id = R.string.edit_art_style_color_blue))
        Slider(
            value = backgroundColor.blue,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = {
                eventReceiver.onEvent(
                    ColorChanged(
                        styleType = StyleType.BACKGROUND,
                        colorType = ColorType.BLUE,
                        changedTo = it
                    )
                )
            }
        )
    }
}