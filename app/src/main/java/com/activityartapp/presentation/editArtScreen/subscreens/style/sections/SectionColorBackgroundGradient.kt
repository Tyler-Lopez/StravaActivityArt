package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ClickedRemoveGradientColor
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewState
import com.activityartapp.presentation.editArtScreen.StyleIdentifier
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorPreview
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorSlidersRGB
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun SectionColorBackgroundGradient(
    colorList: List<ColorWrapper>,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorAdded: (StyleBackgroundColorAdded) -> Unit,
    onColorRemoved: (ClickedRemoveGradientColor) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    colorList.forEachIndexed { index, color ->
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.background
            )
        ) {
            ColumnMediumSpacing(modifier = Modifier.padding(spacing.small)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColorPreview(
                        colorWrapper = color,
                        modifier = Modifier.weight(1f, true)
                    )
                    // For an unknown reason, if this event is not defined
                    // before than a runtime crash occurs
                    val event = ClickedRemoveGradientColor(index)
                    if (colorList.size > EditArtViewState.MIN_GRADIENT_BG_COLORS) {
                        IconButton(onClick = { onColorRemoved(event) }) {
                            Icon(imageVector = Icons.Outlined.Delete, null)
                        }
                    }
                }
                ColorSlidersRGB(
                    color = color,
                    onColorChanged = { colorType, changedTo ->
                        onColorChanged(
                            StyleColorChanged(
                                style = StyleIdentifier.Background(index = index),
                                colorType = colorType,
                                changedTo = changedTo
                            )
                        )
                    },
                    onColorPendingChanged = { colorType, changedTo ->
                        onColorPendingChanged(
                            StyleColorPendingChanged(
                                style = StyleIdentifier.Background(index = index),
                                colorType = colorType,
                                changedTo = changedTo
                            )
                        )
                    },
                    onColorPendingChangeConfirmed = {
                        onColorPendingChangeConfirmed(
                            StyleColorPendingChangeConfirmed(
                                style = StyleIdentifier.Background(index = index),
                            )
                        )
                    }
                )
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        if (colorList.size < EditArtViewState.MAX_GRADIENT_BG_COLORS) {
            Button(
                emphasis = ButtonEmphasis.HIGH,
                size = ButtonSize.MEDIUM,
                text = stringResource(R.string.edit_art_style_background_gradient_add_color_button),
                leadingIcon = Icons.Outlined.Add,
                leadingIconContentDescription = stringResource(R.string.edit_art_style_background_gradient_add_color_button_cd)
            ) {
                onColorAdded(StyleBackgroundColorAdded)
            }
        }
    }
}
