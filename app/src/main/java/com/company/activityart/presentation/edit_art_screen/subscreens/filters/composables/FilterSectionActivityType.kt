package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun FilterSectionActivityType(
    typesWithSelectedFlag: Map<String, Boolean>,
    eventReceiver: EventReceiver<EditArtFiltersViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_filters_activity_type_header),
        description = stringResource(R.string.edit_art_filters_activity_type_description),
    ) {
        typesWithSelectedFlag.forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = it.value, onCheckedChange = { newBoolean ->
                    eventReceiver.onEvent(TypeToggleFlipped(it.key))
                })
                Subhead(it.key)
            }
        }
    }
}