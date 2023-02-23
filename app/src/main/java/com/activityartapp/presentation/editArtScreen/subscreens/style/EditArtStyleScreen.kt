package com.activityartapp.presentation.editArtScreen.subscreens.style

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleBackgroundTypeChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleStrokeWidthChanged
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.*
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.*
import com.activityartapp.util.enums.AngleType
import com.activityartapp.util.enums.BackgroundType

@Composable
fun ColumnScope.EditArtStyleViewDelegate(
    backgroundType: BackgroundType,
    backgroundGradientAngleType: AngleType,
    backgroundGradientColorCount: Int,
    colorBackgroundList: List<ColorWrapper>,
    colorActivities: ColorWrapper,
    colorText: ColorWrapper?,
    strokeWidthType: StrokeWidthType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(header = stringResource(R.string.edit_art_style_background_type_header)) {
        BackgroundType.values().forEach {
            RadioButtonContentRow(
                isSelected = it == backgroundType,
                text = stringResource(it.strRes),
                onHelpPressed = if (it == BackgroundType.TRANSPARENT) {
                    {
                        eventReceiver.onEvent(EditArtViewEvent.ClickedInfoTransparentBackground)
                    }
                } else {
                    null
                }
            ) { eventReceiver.onEvent(StyleBackgroundTypeChanged(changedTo = it)) }
        }
    }
    when (backgroundType) {
        BackgroundType.GRADIENT -> {
            SectionColorBackgroundGradient(
                colorList = colorBackgroundList.take(backgroundGradientColorCount),
                onColorChanged = eventReceiver::onEvent,
                onColorCountChanged = eventReceiver::onEvent,
                onColorPendingChanged = eventReceiver::onEvent,
                onColorPendingChangeConfirmed = eventReceiver::onEvent
            )
            SectionGradientAngle(
                angleType = backgroundGradientAngleType,
                colorList = colorBackgroundList.take(backgroundGradientColorCount),
                onGradientAngleTypeChanged = eventReceiver::onEvent
            )
        }
        BackgroundType.SOLID -> SectionColorBackgroundSolid(
            color = colorBackgroundList.first(),
            onColorChanged = eventReceiver::onEvent,
            onColorPendingChanged = eventReceiver::onEvent,
            onColorPendingChangeConfirmed = eventReceiver::onEvent
        )
        BackgroundType.TRANSPARENT -> {} // No-op
    }
    SectionColorActivities(
        color = colorActivities,
        onColorChanged = eventReceiver::onEvent,
        onColorPendingChanged = eventReceiver::onEvent,
        onColorPendingChangeConfirmed = eventReceiver::onEvent
    )
    SectionColorText(
        color = colorText,
        colorActivities = colorActivities,
        onColorChanged = eventReceiver::onEvent,
        onUseFontChanged = eventReceiver::onEvent,
        onColorPendingChanged = eventReceiver::onEvent,
        onColorPendingChangeConfirmed = eventReceiver::onEvent
    )
    Section(
        header = stringResource(R.string.edit_art_style_stroke_width_header),
        description = stringResource(R.string.edit_art_style_stroke_width_description)
    ) {
        StrokeWidthType.values().forEach {
            RadioButtonContentRow(
                isSelected = strokeWidthType == it,
                text = stringResource(id = it.headerId)
            ) { eventReceiver.onEvent(StyleStrokeWidthChanged(it)) }
        }
    }
}