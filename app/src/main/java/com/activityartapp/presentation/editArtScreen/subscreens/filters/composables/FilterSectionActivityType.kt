package com.activityartapp.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterSection

@Composable
fun ColumnScope.FilterSectionActivityType(
    count: Int,
    typesWithSelectedFlag: List<Pair<String, Boolean>>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    FilterSection(
        count = count,
        description = stringResource(R.string.edit_art_filters_activity_type_description),
        header = stringResource(R.string.edit_art_filters_activity_type_header)
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
                            EditArtViewEvent.ArtMutatingEvent.FilterChanged.FilterTypeToggled(
                                typeMap.first
                            )
                        )
                    })
                Subhead(typeMap.first)
            }
        }
    }
}