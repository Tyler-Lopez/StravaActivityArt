package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleStrokeWidthChanged
import com.activityartapp.presentation.editArtScreen.StrokeWidthType
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow

@Composable
fun SectionActivityWidth(
    strokeWidthType: StrokeWidthType,
    onStyleStrokeWidthChanged: (StyleStrokeWidthChanged) -> Unit
) {
    StrokeWidthType.values().forEach {
        RadioButtonContentRow(
            isSelected = strokeWidthType == it,
            text = stringResource(id = it.headerId)
        ) { onStyleStrokeWidthChanged(StyleStrokeWidthChanged(it)) }
    }
}