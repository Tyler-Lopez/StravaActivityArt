package com.company.activityart.presentation.editArtScreen.subscreens.type

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtTypeScreen(
    scrollState: ScrollState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        EditArtTypeSection.values().forEach { section ->
            Section(
                header = stringResource(section.header),
                description = stringResource(section.description)
            ) {
                EditArtTypeType.values().forEach { type ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = false, onClick = {
                            /*
                            eventReceiver.onEvent(
                                EditArtViewEvent.ArtMutatingEvent.StylesStrokeWidthChanged(it)
                            )
                            */
                        })
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Body(text = stringResource(type.header))
                            when (type) {
                                EditArtTypeType.NONE -> {}
                                EditArtTypeType.NAME -> SubheadHeavy(text = "Tyler Lopez")
                                EditArtTypeType.DISTANCE_MILES -> SubheadHeavy(text = "234 mi")
                                EditArtTypeType.DISTANCE_KILOMETERS -> SubheadHeavy(text = "521 km")
                                EditArtTypeType.CUSTOM -> TextField("", {}, enabled = false)
                            }
                        }
                    }
                }
            }
        }
    }
}
