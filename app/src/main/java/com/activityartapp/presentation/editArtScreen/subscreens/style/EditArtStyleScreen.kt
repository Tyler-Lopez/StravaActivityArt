package com.activityartapp.presentation.editArtScreen.subscreens.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleBackgroundTypeChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StylesStrokeWidthChanged
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.presentation.editArtScreen.*
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonWithContent
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.*
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
        ColumnSmallSpacing {
            BackgroundType.values().forEach {
                RadioButtonWithContent(
                    isSelected = it == backgroundType,
                    text = stringResource(it.strRes)
                ) { eventReceiver.onEvent(StyleBackgroundTypeChanged(changedTo = it)) }
            }
        }
    }
    SectionColorBackground(
        backgroundType = backgroundType,
        colors = backgroundColors,
        onColorChanged = eventReceiver::onEvent
    )
    SectionColorActivities(color = colorActivities, onColorChanged = eventReceiver::onEvent)
    SectionColorFont(
        color = colorFont,
        colorActivities = colorActivities,
        onColorChanged = eventReceiver::onEvent,
        onUseFontChanged = eventReceiver::onEvent
    )
    Section(
        header = stringResource(R.string.edit_art_style_stroke_width_header),
        description = stringResource(R.string.edit_art_style_stroke_width_description)
    ) {
        ColumnSmallSpacing {
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
}