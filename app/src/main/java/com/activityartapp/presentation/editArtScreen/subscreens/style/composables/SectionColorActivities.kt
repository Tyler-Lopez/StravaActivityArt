package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorActivitiesChanged
import com.activityartapp.presentation.editArtScreen.composables.Section

@Composable
fun ColumnScope.SectionColorActivities(
    color: ColorWrapper,
    onColorChanged: (StyleColorActivitiesChanged) -> Unit
) {
    Section(
        header = stringResource(R.string.edit_art_style_activities_header),
        description = stringResource(R.string.edit_art_style_activities_description)
    ) {
        ColorPreview(colorWrapper = color)
        ColorSlidersRGB(color = color, enabled = true) {
            onColorChanged(
                StyleColorActivitiesChanged(
                    colorType = it.first,
                    changedTo = it.second
                )
            )
        }
    }
}