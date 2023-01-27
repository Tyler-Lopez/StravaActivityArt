package com.activityartapp.presentation.editArtScreen.subscreens.sort

import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.EditArtSortDirectionType
import com.activityartapp.util.enums.EditArtSortType


@Composable
fun ColumnScope.EditArtSortScreen(
    sortTypeSelected: EditArtSortType,
    sortDirectionSelected: EditArtSortDirectionType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Section(
        header = stringResource(R.string.edit_art_sort_header),
        description = stringResource(R.string.edit_art_sort_description)
    ) {
        EditArtSortType.values().forEach { sortType ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortType == sortTypeSelected,
                    onClick = {
                        eventReceiver.onEvent(
                            EditArtViewEvent.ArtMutatingEvent.SortTypeChanged(
                                sortType
                            )
                        )
                    }
                )
                Subhead(text = stringResource(sortType.strRes))
            }
        }
    }
    Section(
        header = stringResource(R.string.edit_art_sort_direction_header),
        description = stringResource(R.string.edit_art_sort_direction_description)
    ) {
        EditArtSortDirectionType.values().forEach { sortDirectionType ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortDirectionType == sortDirectionSelected,
                    onClick = {
                        eventReceiver.onEvent(
                            EditArtViewEvent.ArtMutatingEvent.SortDirectionChanged(
                                sortDirectionType
                            )
                        )
                    }
                )
                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                    Subhead(text = stringResource(sortDirectionType.headerStrRes))
                    SubheadHeavy(
                        text = stringResource(
                            id = sortDirectionType.description(
                                sortTypeSelected
                            )
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}