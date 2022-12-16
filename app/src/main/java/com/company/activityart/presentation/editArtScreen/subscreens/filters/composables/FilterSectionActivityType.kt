package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun FilterSectionActivityType(
    typesWithSelectedFlag: List<Pair<String, Boolean>>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_filters_activity_type_header),
        description = stringResource(R.string.edit_art_filters_activity_type_description),
    ) {
        typesWithSelectedFlag.forEach { typeMap ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = typeMap.second,
                    onCheckedChange = {
                        eventReceiver.onEvent(
                            EditArtViewEvent.ArtMutatingEvent.FilterTypeToggled(typeMap.first)
                        )
                    })
                Subhead(typeMap.first)
            }
        }
    }
}