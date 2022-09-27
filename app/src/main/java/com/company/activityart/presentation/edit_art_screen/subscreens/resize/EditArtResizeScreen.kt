package com.company.activityart.presentation.edit_art_screen.subscreens.resize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.SizeChanged
import com.company.activityart.presentation.edit_art_screen.Resolution
import com.company.activityart.presentation.edit_art_screen.Resolution.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtResizeScreen(
    resolutionList: List<Resolution>,
    selectedResolutionIndex: Int,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Section(
            header = stringResource(R.string.edit_art_resize_header),
            description = stringResource(R.string.edit_art_resize_description),
        ) {
            resolutionList.forEachIndexed { index, res ->
                val isSelected = selectedResolutionIndex == index
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(spacing.medium)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            eventReceiver.onEvent(SizeChanged(index))
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Body(text = res.displayTextResolution())
                            when (res) {
                                is CustomResolution -> {
                                    TextField(
                                        value = res.widthPx.toString(),
                                        enabled = isSelected,
                                        placeholder = { Text("0") },
                                        onValueChange = {

                                        },
                                        leadingIcon = { SubheadHeavy(text = "Width") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )
                                    )
                                    TextField(
                                        value = res.heightPx.toString(),
                                        enabled = isSelected,
                                        placeholder = { Text("0") },
                                        onValueChange = {

                                        },
                                        leadingIcon = { SubheadHeavy(text = "Height") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )
                                    )
                                }
                                is SwappableResolution -> SubheadHeavy(text = res.displayTextPixels())
                            }
                        }

                        if (res is SwappableResolution && res.swappingChangesSize) {
                            MediumEmphasisButton(
                                imageVector = Icons.Default.RotateRight,
                                size = ButtonSize.MEDIUM,
                                onClick = {
                                    eventReceiver.onEvent(
                                        EditArtViewEvent.ArtMutatingEvent.SizeRotated(
                                            index
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}