package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ClickedInfoTransparentBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ClickedInfoGradientBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleBackgroundTypeChanged
import com.activityartapp.util.enums.BackgroundType

@Composable
fun SectionBackgroundType(
    backgroundType: BackgroundType,
    onBackgroundTypeChanged: (StyleBackgroundTypeChanged) -> Unit,
    onClickedInfoGradientBackground: (ClickedInfoGradientBackground) -> Unit,
    onClickedInfoTransparentBackground: (ClickedInfoTransparentBackground) -> Unit
) {
    BackgroundType.values().forEach {
        RadioButtonContentRow(
            isSelected = it == backgroundType,
            text = stringResource(it.strRes),
            onHelpPressed = when (it) {
                BackgroundType.GRADIENT -> {
                    { onClickedInfoGradientBackground(ClickedInfoGradientBackground) }
                }
                BackgroundType.TRANSPARENT -> {
                    { onClickedInfoTransparentBackground(ClickedInfoTransparentBackground) }
                }
                else -> null
            },
            onSelected = {
                onBackgroundTypeChanged(StyleBackgroundTypeChanged(changedTo = it))
            })
    }
}