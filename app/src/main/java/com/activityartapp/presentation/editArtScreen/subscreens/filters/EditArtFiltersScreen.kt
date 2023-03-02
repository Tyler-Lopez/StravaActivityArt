package com.activityartapp.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
    activitiesCountDate: State<Int>,
    activitiesCountDistance: State<Int>,
    activitiesCountType: State<Int>,
    filterDateSelections: SnapshotStateList<DateSelection>,
    filterDateSelectionIndex: State<Int>,
    distanceSelectedStart: State<Double?>,
    distanceSelectedEnd: State<Double?>,
    distanceTotalStart: State<Double?>,
    distanceTotalEnd: State<Double?>,
    distancePendingChangeStart: State<String?>,
    distancePendingChangeEnd: State<String?>,
    listState: LazyListState,
    typesWithSelectedFlag: SnapshotStateMap<SportType, Boolean>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val sections = mutableListOf<EditArtFiltersSectionType>().apply {
        if (filterDateSelections.isNotEmpty()) {
            add(EditArtFiltersSectionType.DATE)
        }
        if (typesWithSelectedFlag.isNotEmpty()) {
            add(EditArtFiltersSectionType.ACTIVITY_TYPE)
        }
        if (distanceTotalStart.value != null) { // todo add back start != end case
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
                    EditArtFiltersSectionType.DATE -> SectionDate(
                        count = activitiesCountDate.value,
                        dateSelections = filterDateSelections,
                        dateSelectionSelectedIndex = filterDateSelectionIndex.value,
                        eventReceiver = eventReceiver
                    )
                    EditArtFiltersSectionType.ACTIVITY_TYPE -> SectionActivityType(
                        count = activitiesCountType.value,
                        typesWithSelectedFlag = typesWithSelectedFlag.toList(),
                        eventReceiver = eventReceiver
                    )
                    EditArtFiltersSectionType.DISTANCE -> distanceTotalEnd.value?.let {
                        distanceTotalStart.value?.rangeTo(it)
                    }?.let {
                        SectionDistances(
                            count = activitiesCountDistance.value,
                            distanceSelected = distanceSelectedEnd.value?.let { end ->
                                distanceSelectedStart.value?.rangeTo(end)
                            },
                            distanceTotal = it,
                            distancePendingChangeStart = distancePendingChangeStart.value,
                            distancePendingChangeEnd = distancePendingChangeEnd.value,
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