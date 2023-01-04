package com.company.activityart.presentation.editArtScreen.subscreens.type

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing
import kotlin.text.Typography.section

@Composable
fun EditArtTypeScreen(
    customTextCenter: String,
    customTextLeft: String,
    customTextRight: String,
    maximumCustomTextLength: Int,
    selectedEditArtTypeTypeCenter: EditArtTypeType,
    selectedEditArtTypeTypeLeft: EditArtTypeType,
    selectedEditArtTypeTypeRight: EditArtTypeType,
    scrollState: ScrollState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val focusManager = LocalFocusManager.current

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
                        RadioButton(
                            selected = type == when (section) {
                                EditArtTypeSection.LEFT -> selectedEditArtTypeTypeLeft
                                EditArtTypeSection.CENTER -> selectedEditArtTypeTypeCenter
                                EditArtTypeSection.RIGHT -> selectedEditArtTypeTypeRight
                            },
                            onClick = {
                                eventReceiver.onEvent(
                                    EditArtViewEvent.ArtMutatingEvent.TypeSelectionChanged(
                                        section = section,
                                        typeSelected = type
                                    )
                                )

                            })
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Body(text = stringResource(type.header))
                            when (type) {
                                EditArtTypeType.NONE -> {}
                                EditArtTypeType.NAME -> SubheadHeavy(text = "Tyler Lopez")
                                EditArtTypeType.DISTANCE_MILES -> SubheadHeavy(text = "234 mi")
                                EditArtTypeType.DISTANCE_KILOMETERS -> SubheadHeavy(text = "521 km")
                                EditArtTypeType.CUSTOM -> {
                                    OutlinedTextField(
                                        value = when (section) {
                                            EditArtTypeSection.LEFT -> customTextLeft
                                            EditArtTypeSection.CENTER -> customTextCenter
                                            EditArtTypeSection.RIGHT -> customTextRight
                                        },
                                        onValueChange = {
                                            eventReceiver.onEvent(
                                                EditArtViewEvent.ArtMutatingEvent.TypeCustomTextChanged(
                                                    section = section,
                                                    changedTo = it
                                                )
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            autoCorrect = false,
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = { focusManager.clearFocus() }
                                        ),
                                        singleLine = true,
                                        maxLines = 1,
                                        enabled = EditArtTypeType.CUSTOM == when (section) {
                                            EditArtTypeSection.LEFT -> selectedEditArtTypeTypeLeft
                                            EditArtTypeSection.CENTER -> selectedEditArtTypeTypeCenter
                                            EditArtTypeSection.RIGHT -> selectedEditArtTypeTypeRight
                                        },
                                        modifier = Modifier.sizeIn(maxWidth = 254.dp)
                                    )
                                    SubheadHeavy(
                                        text = "${
                                            when (section) {
                                                EditArtTypeSection.LEFT -> customTextLeft.length
                                                EditArtTypeSection.CENTER -> customTextCenter.length
                                                EditArtTypeSection.RIGHT -> customTextRight.length
                                            }
                                        } / $maximumCustomTextLength"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
