package com.company.activityart.presentation.edit_art_screen.subscreens.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.StyleType.*
import com.company.activityart.presentation.edit_art_screen.subscreens.style.composables.StyleTypeSliders
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtStyleViewDelegate(
    colorActivities: ColorWrapper,
    colorBackground: ColorWrapper,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
    }
}