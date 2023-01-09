package com.company.activityart.presentation.editArtScreen.subscreens.filters

import android.graphics.Typeface
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.editArtScreen.DateSelection
import com.company.activityart.presentation.editArtScreen.EditArtFilterType
import com.company.activityart.presentation.editArtScreen.EditArtFilterType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionActivityType
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDate
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDistances
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay

@Composable
fun EditArtFiltersScreen(
    activitiesCountDate: Int,
    activitiesCountDistance: Int,
    activitiesCountType: Int,
    filterDateSelections: List<DateSelection>?,
    filterDateSelectionIndex: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>?,
    typesWithSelectedFlag: List<Pair<String, Boolean>>,
    scrollState: ScrollState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        EditArtFilterType.values().onEach {
            when (it) {
                DATE -> if (filterDateSelections != null)
                    FilterSectionDate(
                        count = activitiesCountDate,
                        dateSelections = filterDateSelections,
                        dateSelectionSelectedIndex = filterDateSelectionIndex,
                        eventReceiver = eventReceiver
                    )
                TYPE -> FilterSectionActivityType(
                    count = activitiesCountType,
                    typesWithSelectedFlag = typesWithSelectedFlag,
                    eventReceiver = eventReceiver
                )
                DISTANCE -> if (distanceTotal != null) {
                    FilterSectionDistances(
                        count = activitiesCountDistance,
                        distanceSelected = distanceSelected,
                        distanceTotal = distanceTotal,
                        eventReceiver = eventReceiver
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}