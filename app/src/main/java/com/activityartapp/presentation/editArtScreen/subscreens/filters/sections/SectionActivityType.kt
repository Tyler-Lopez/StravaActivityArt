package com.activityartapp.presentation.editArtScreen.subscreens.filters.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterCount
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.SportType

@Composable
fun SectionActivityType(
    count: Int,
    typesWithSelectedFlag: List<Pair<SportType, Boolean>>,
    eventReceiver: EventReceiver<EditArtViewEvent>
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
            Text(
                text = stringResource(typeMap.first.stringRes),
                style = MaterialTheme.typography.body1
            )
        }
    }
    FilterCount(count)
}