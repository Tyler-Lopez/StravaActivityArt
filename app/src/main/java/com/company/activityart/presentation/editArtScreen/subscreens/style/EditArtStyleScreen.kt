package com.company.activityart.presentation.editArtScreen.subscreens.style

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.editArtScreen.ColorWrapper
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.company.activityart.presentation.editArtScreen.StyleType.*
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.editArtScreen.subscreens.style.composables.StyleTypeSliders
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtStyleViewDelegate(
    colorActivities: ColorWrapper,
    colorBackground: ColorWrapper,
    scrollState: ScrollState,
    strokeWidthType: StrokeWidthType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        StyleTypeSliders(
            header = stringResource(id = R.string.edit_art_style_background_header),
            description = stringResource(id = R.string.edit_art_style_background_description),
            styleType = BACKGROUND,
            color = colorBackground,
            eventReceiver = eventReceiver
        )
        StyleTypeSliders(
            header = stringResource(id = R.string.edit_art_style_activities_header),
            description = stringResource(id = R.string.edit_art_style_activities_description),
            styleType = ACTIVITIES,
            color = colorActivities,
            eventReceiver = eventReceiver
        )
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
}