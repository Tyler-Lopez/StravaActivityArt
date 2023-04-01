package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ClickedRemoveActivityColorRule
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleColorPendingChangeConfirmed
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.StyleColorPendingChanged
import com.activityartapp.presentation.editArtScreen.EditArtViewState
import com.activityartapp.presentation.editArtScreen.StyleIdentifier
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorPreview
import com.activityartapp.presentation.editArtScreen.subscreens.style.composables.ColorSlidersRGB
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.ActivityColorRule

@Composable
fun SectionColorActivities(
    colorRules: List<ActivityColorRule>,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit,
    onColorRemoved: (ClickedRemoveActivityColorRule) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacing.medium, end = spacing.medium, bottom = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.edit_art_style_activities_description),
            style = MaterialTheme.typography.body1
        )
        Text(
            text = stringResource(R.string.edit_art_style_activities_description_additional_1),
            style = MaterialTheme.typography.body1
        )
        Text(
            text = stringResource(R.string.edit_art_style_activities_description_additional_2),
            style = MaterialTheme.typography.body1
        )
    }

    val lazyListState = rememberLazyListState()
    val colorCount = colorRules.size
    LazyRow(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(vertical = spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.xSmall)
    ) {
        item { Spacer(modifier = Modifier.width(spacing.xSmall)) }
        items(colorCount) {
            ColorRuleItem(
                index = it,
                color = colorRules[it],
                colorsCount = colorCount,
                onColorChanged = onColorChanged,
                onColorRemoved = onColorRemoved,
                onColorPendingChangeConfirmed = onColorPendingChangeConfirmed,
                onColorPendingChanged = onColorPendingChanged
            )
        }
        item { Spacer(modifier = Modifier.width(spacing.xSmall)) }
    }
    /*
    ColorPreview(colorWrapper = color)
    ColorSlidersRGB(
        color = color,
        onColorChanged = { colorType, changedTo ->
            onColorChanged(
                StyleColorChanged(
                    style = StyleIdentifier.Activities,
                    colorType = colorType,
                    changedTo = changedTo
                )
            )
        },
        onColorPendingChanged = { colorType, changedTo ->
            onColorPendingChanged(
                StyleColorPendingChanged(
                    style = StyleIdentifier.Activities,
                    colorType = colorType,
                    changedTo = changedTo
                )
            )
        },
        onColorPendingChangeConfirmed = {
            onColorPendingChangeConfirmed(
                StyleColorPendingChangeConfirmed(
                    style = StyleIdentifier.Activities
                )
            )
        }
    )
    
     */
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColorRuleItem(
    index: Int,
    color: ActivityColorRule,
    colorsCount: Int,
    onColorChanged: (StyleColorChanged) -> Unit,
    onColorRemoved: (ClickedRemoveActivityColorRule) -> Unit,
    onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
    onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
    val colorWrapper = color.color
    var isContextMenuVisible = remember { mutableStateOf(false) }
    Card {
        ColumnMediumSpacing(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.small)
        ) {
            // Header information, either RULE 1 / 2 or DEFAULT
            if (color is ActivityColorRule.Any) {
                Text(
                    text = stringResource(R.string.edit_art_style_activities_default),
                    style = MaterialTheme.typography.subtitle2
                )
            } else {
                Text(
                    text = "RULE ${index + 1} / ${colorsCount - 1}",
                    style = MaterialTheme.typography.subtitle2,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.medium)) {
                if (color !is ActivityColorRule.Any) {
                    Column(
                        modifier = Modifier.width(254.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(space = spacing.small)
                    ) {
                        var expanded = false // todo
                        for (i in 0..3) {
                            Text(
                                text = "RULE TYPE",
                                style = MaterialTheme.typography.subtitle2
                            )
                            ExposedDropdownMenuBox(
                                expanded = false,//
                                onExpandedChange = {
                                    //  expanded = !expanded
                                }
                            ) {
                                TextField(
                                    value = "Distance",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = false
                                        )
                                    }, //
                                    //  modifier = Modifier.menu
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    /*
                                    coffeeDrinks.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                selectedText = item
                                                expanded = false
                                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }

                                     */
                                }
                            }
                        }
                    }
                }
                ColumnMediumSpacing(modifier = Modifier.width(360.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ColorPreview(
                            colorWrapper = colorWrapper,
                            modifier = Modifier.weight(1f, true)
                        )
                        // For an unknown reason, if this event is not defined
                        // before than a runtime crash occurs
                        val event = ClickedRemoveActivityColorRule(index)
                        if (colorsCount > 1 && color !is ActivityColorRule.Any) { // todo add constant
                            IconButton(onClick = { onColorRemoved(event) }) {
                                Icon(imageVector = Icons.Outlined.Delete, null) // todo cd
                            }
                        }
                    }
                    ColorSlidersRGB(
                        color = colorWrapper,
                        onColorChanged = { colorType, changedTo ->
                            onColorChanged(
                                StyleColorChanged(
                                    style = StyleIdentifier.Activities(index = index),
                                    colorType = colorType,
                                    changedTo = changedTo
                                )
                            )
                        },
                        onColorPendingChanged = { colorType, changedTo ->
                            onColorPendingChanged(
                                StyleColorPendingChanged(
                                    style = StyleIdentifier.Activities(index = index),
                                    colorType = colorType,
                                    changedTo = changedTo
                                )
                            )
                        },
                        onColorPendingChangeConfirmed = {
                            onColorPendingChangeConfirmed(
                                StyleColorPendingChangeConfirmed(
                                    style = StyleIdentifier.Activities(index = index),
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}


private enum class DropdownRuleType {
    DISTANCE, TYPE
}
/**
@Composable
fun SectionColorBackgroundGradient(
colorList: List<ColorWrapper>,
colorsCount: State<Int>,
onColorChanged: (StyleColorChanged) -> Unit,
onColorAdded: (EditArtViewEvent.ArtMutatingEvent.StyleBackgroundColorAdded) -> Unit,
onColorRemoved: (EditArtViewEvent.ClickedRemoveGradientColor) -> Unit,
onColorPendingChangeConfirmed: (StyleColorPendingChangeConfirmed) -> Unit,
onColorPendingChanged: (StyleColorPendingChanged) -> Unit
) {
val lazyListState = rememberLazyListState()
LazyRow(
state = lazyListState,
modifier = Modifier
.padding(
bottom = if ((colorsCount.value >= EditArtViewState.MAX_GRADIENT_BG_COLORS)) {
spacing.medium
} else {
0.dp
}
)
.background(MaterialTheme.colors.background)
.padding(vertical = spacing.small),
horizontalArrangement = Arrangement.spacedBy(spacing.xSmall)
) {
item { Spacer(modifier = Modifier.width(spacing.xSmall)) }
items(colorsCount.value) {
val color = colorList[it]
ListItem(
index = it,
color = color,
colorsCount = colorsCount.value,
onColorChanged = onColorChanged,
onColorRemoved = onColorRemoved,
onColorPendingChangeConfirmed = onColorPendingChangeConfirmed,
onColorPendingChanged = onColorPendingChanged
)
}
item { Spacer(modifier = Modifier.width(spacing.xSmall)) }
}

if (colorsCount.value < EditArtViewState.MAX_GRADIENT_BG_COLORS) {
Column(
modifier = Modifier
.fillMaxWidth()
.padding(bottom = spacing.medium, end = spacing.medium),
horizontalAlignment = Alignment.End,
) {
Button(
emphasis = ButtonEmphasis.HIGH,
size = ButtonSize.MEDIUM,
text = stringResource(R.string.edit_art_style_background_gradient_add_color_button),
leadingIcon = Icons.Outlined.Add,
leadingIconContentDescription = stringResource(R.string.edit_art_style_background_gradient_add_color_button_cd)
) {
onColorAdded(EditArtViewEvent.ArtMutatingEvent.StyleBackgroundColorAdded)
}
}
}
}

 **/