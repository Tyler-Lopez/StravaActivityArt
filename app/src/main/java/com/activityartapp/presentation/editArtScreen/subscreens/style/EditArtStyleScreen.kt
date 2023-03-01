package com.activityartapp.presentation.editArtScreen.subscreens.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.StrokeWidthType
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.*
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.AngleType
import com.activityartapp.util.enums.BackgroundType

@Composable
fun EditArtStyleScreen(
    backgroundType: State<BackgroundType>,
    backgroundGradientAngleType: State<AngleType>,
    colorBackgroundList: SnapshotStateList<ColorWrapper>,
    colorActivities: State<ColorWrapper>,
    colorText: State<ColorWrapper?>,
    listState: LazyListState,
    strokeWidthType: State<StrokeWidthType>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val sections = mutableListOf<EditArtStyleSectionType>().apply {
        add(EditArtStyleSectionType.BACKGROUND_TYPE)
        when (backgroundType.value) {
            BackgroundType.GRADIENT -> {
                add(EditArtStyleSectionType.GRADIENT_ANGLE)
                add(EditArtStyleSectionType.GRADIENT_COLORS)
            }
            BackgroundType.SOLID -> add(EditArtStyleSectionType.SOLID_COLOR)
            BackgroundType.TRANSPARENT -> {} // No-op
        }
        add(EditArtStyleSectionType.ACTIVITIES_COLOR)
        add(EditArtStyleSectionType.FONT_COLOR)
        add(EditArtStyleSectionType.ACTIVITY_WEIGHT)
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        items(sections) { section ->
            Section(
                header = stringResource(section.headerStrRes),
                description = section.descriptionStrRes?.let { stringResource(it) }
            ) {
                when (section) {
                    EditArtStyleSectionType.BACKGROUND_TYPE -> SectionBackgroundType(
                        backgroundType = backgroundType.value,
                        onBackgroundTypeChanged = eventReceiver::onEvent,
                        onClickedInfoGradientBackground = eventReceiver::onEvent,
                        onClickedInfoTransparentBackground = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.GRADIENT_ANGLE -> SectionGradientAngle(
                        angleType = backgroundGradientAngleType.value,
                        colorList = colorBackgroundList,
                        onGradientAngleTypeChanged = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.GRADIENT_COLORS -> SectionColorBackgroundGradient(
                        colorList = colorBackgroundList,
                        onColorChanged = eventReceiver::onEvent,
                        onColorAdded = eventReceiver::onEvent,
                        onColorRemoved = eventReceiver::onEvent,
                        onColorPendingChangeConfirmed = eventReceiver::onEvent,
                        onColorPendingChanged = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.SOLID_COLOR -> SectionColorBackgroundSolid(
                        color = colorBackgroundList.first(),
                        onColorChanged = eventReceiver::onEvent,
                        onColorPendingChangeConfirmed = eventReceiver::onEvent,
                        onColorPendingChanged = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.ACTIVITIES_COLOR -> SectionColorActivities(
                        color = colorActivities.value,
                        onColorChanged = eventReceiver::onEvent,
                        onColorPendingChanged = eventReceiver::onEvent,
                        onColorPendingChangeConfirmed = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.FONT_COLOR -> SectionColorText(
                        color = colorText.value,
                        colorActivities = colorActivities.value,
                        onColorChanged = eventReceiver::onEvent,
                        onUseFontChanged = eventReceiver::onEvent,
                        onColorPendingChanged = eventReceiver::onEvent,
                        onColorPendingChangeConfirmed = eventReceiver::onEvent
                    )
                    EditArtStyleSectionType.ACTIVITY_WEIGHT -> SectionActivityWidth(
                        strokeWidthType = strokeWidthType.value,
                        onStyleStrokeWidthChanged = eventReceiver::onEvent
                    )
                }
            }
        }
    }
}

private enum class EditArtStyleSectionType(
    override val headerStrRes: Int,
    override val descriptionStrRes: Int?
) : Section {
    BACKGROUND_TYPE(
        headerStrRes = R.string.edit_art_style_background_type_header,
        descriptionStrRes = R.string.edit_art_style_background_type_description
    ),
    GRADIENT_ANGLE(
        headerStrRes = R.string.edit_art_style_background_gradient_angle_header,
        descriptionStrRes = R.string.edit_art_style_background_gradient_angle_description
    ),
    GRADIENT_COLORS(
        headerStrRes = R.string.edit_art_style_background_gradient_header,
        descriptionStrRes = R.string.edit_art_style_background_gradient_description
    ),
    SOLID_COLOR(
        headerStrRes = R.string.edit_art_style_background_solid_header,
        descriptionStrRes = R.string.edit_art_style_background_solid_description
    ),
    ACTIVITIES_COLOR(
        headerStrRes = R.string.edit_art_style_activities_header,
        descriptionStrRes = R.string.edit_art_style_activities_description
    ),
    FONT_COLOR(
        headerStrRes = R.string.edit_art_style_text_header,
        descriptionStrRes = null
    ),
    ACTIVITY_WEIGHT(
        headerStrRes = R.string.edit_art_style_stroke_width_header,
        descriptionStrRes = R.string.edit_art_style_stroke_width_description
    );
}