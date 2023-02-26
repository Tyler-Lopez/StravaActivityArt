package com.activityartapp.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.subscreens.filters.sections.SectionActivityType
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.subscreens.filters.sections.SectionDate
import com.activityartapp.presentation.editArtScreen.subscreens.filters.sections.SectionDistances
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.SportType

@Composable
fun EditArtFiltersScreen(
    activitiesCountDate: Int,
    activitiesCountDistance: Int,
    activitiesCountType: Int,
    filterDateSelections: List<DateSelection>?,
    filterDateSelectionIndex: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>?,
    distancePendingChangeStart: String?,
    distancePendingChangeEnd: String?,
    listState: LazyListState,
    typesWithSelectedFlag: List<Pair<SportType, Boolean>>?,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val sections = mutableListOf<EditArtFiltersSectionType>().apply {
        if (filterDateSelections != null) {
            add(EditArtFiltersSectionType.DATE)
        }
        if (typesWithSelectedFlag != null) {
            add(EditArtFiltersSectionType.ACTIVITY_TYPE)
        }
        if (distanceTotal != null && distanceTotal.start != distanceTotal.endInclusive) {
            add(EditArtFiltersSectionType.DISTANCE)
        }
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
                    EditArtFiltersSectionType.DATE -> filterDateSelections?.let {
                        SectionDate(
                            count = activitiesCountDate,
                            dateSelections = it,
                            dateSelectionSelectedIndex = filterDateSelectionIndex,
                            eventReceiver = eventReceiver
                        )
                    }
                    EditArtFiltersSectionType.ACTIVITY_TYPE -> typesWithSelectedFlag?.let {
                        SectionActivityType(
                            count = activitiesCountType,
                            typesWithSelectedFlag = typesWithSelectedFlag,
                            eventReceiver = eventReceiver
                        )
                    }
                    EditArtFiltersSectionType.DISTANCE -> distanceTotal?.let {
                        SectionDistances(
                            count = activitiesCountDistance,
                            distanceSelected = distanceSelected,
                            distanceTotal = it,
                            distancePendingChangeStart = distancePendingChangeStart,
                            distancePendingChangeEnd = distancePendingChangeEnd,
                            eventReceiver = eventReceiver
                        )
                    }
                }
            }
        }
    }
}


private enum class EditArtFiltersSectionType(
    override val headerStrRes: Int,
    override val descriptionStrRes: Int?
) : Section {
    DATE(
        headerStrRes = R.string.edit_art_filters_date_header,
        descriptionStrRes = R.string.edit_art_filters_date_description
    ),
    ACTIVITY_TYPE(
        headerStrRes = R.string.edit_art_filters_activity_type_header,
        descriptionStrRes = R.string.edit_art_filters_activity_type_description
    ),
    DISTANCE(
        headerStrRes = R.string.edit_art_filters_distance_header,
        descriptionStrRes = R.string.edit_art_filters_distance_description
    );
}