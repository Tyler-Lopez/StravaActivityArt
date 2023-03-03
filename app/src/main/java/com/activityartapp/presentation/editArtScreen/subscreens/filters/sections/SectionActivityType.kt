package com.activityartapp.presentation.editArtScreen.subscreens.filters.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterChanged
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterCount
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.SportType

@Composable
fun SectionActivityType(
    count: Int,
    typesWithSelectedFlag: SnapshotStateList<Pair<SportType, Boolean>>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    TypeRows(
        typesWithSelectedFlag = typesWithSelectedFlag,
        onFilterTypeToggled = eventReceiver::onEvent
    )
    FilterCount(count)
}

@Composable
private fun TypeRows(
    typesWithSelectedFlag: List<Pair<SportType, Boolean>>,
    onFilterTypeToggled: (FilterChanged.FilterTypeToggled) -> Unit
) {
    typesWithSelectedFlag.forEach { selection ->
        TypeRowItem(
            typeWithSelectedFlag = selection,
            onFilterTypeToggled = onFilterTypeToggled
        )
    }
}

@Composable
private fun TypeRowItem(
    typeWithSelectedFlag: Pair<SportType, Boolean>,
    onFilterTypeToggled: (FilterChanged.FilterTypeToggled) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = typeWithSelectedFlag.second,
            onCheckedChange = {
                onFilterTypeToggled(
                    FilterChanged.FilterTypeToggled(
                        type = typeWithSelectedFlag.first
                    )
                )
            })
        Text(
            text = stringResource(typeWithSelectedFlag.first.stringRes),
            style = MaterialTheme.typography.body1
        )
    }
}
