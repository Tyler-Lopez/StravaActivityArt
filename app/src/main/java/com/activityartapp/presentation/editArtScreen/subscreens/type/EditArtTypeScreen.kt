package com.activityartapp.presentation.editArtScreen.subscreens.type

import android.graphics.Typeface
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import com.activityartapp.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.composables.RadioButtonContentRow
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.FontSizeType
import com.activityartapp.util.enums.FontType
import com.activityartapp.util.enums.FontWeightType
import kotlin.math.roundToInt

@Composable
fun EditArtTypeScreen(
    activitiesDistanceMetersSummed: State<Int>,
    customTextCenter: State<String>,
    customTextLeft: State<String>,
    customTextRight: State<String>,
    fontSelected: State<FontType>,
    fontWeightSelected: State<FontWeightType>,
    fontItalicized: State<Boolean>,
    fontSizeSelected: State<FontSizeType>,
    maximumCustomTextLength: Int,
    selectedEditArtTypeTypeCenter: State<EditArtTypeType>,
    selectedEditArtTypeTypeLeft: State<EditArtTypeType>,
    selectedEditArtTypeTypeRight: State<EditArtTypeType>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    EditArtTypeSection.values().forEach { section ->
        Section(
            header = stringResource(section.header),
            description = stringResource(section.description)
        ) {
            EditArtTypeType.values().forEach { type ->
                RadioButtonContentRow(
                    isSelected = type == when (section) {
                        EditArtTypeSection.LEFT -> selectedEditArtTypeTypeLeft.value
                        EditArtTypeSection.CENTER -> selectedEditArtTypeTypeCenter.value
                        EditArtTypeSection.RIGHT -> selectedEditArtTypeTypeRight.value
                    },
                    content = type.takeIf { it == EditArtTypeType.CUSTOM }?.let {
                        {
                            ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
                                OutlinedTextField(
                                    value = when (section) {
                                        EditArtTypeSection.LEFT -> customTextLeft.value
                                        EditArtTypeSection.CENTER -> customTextCenter.value
                                        EditArtTypeSection.RIGHT -> customTextRight.value
                                    },
                                    onValueChange = {
                                        eventReceiver.onEvent(
                                            TypeCustomTextChanged(
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
                                    modifier = Modifier.sizeIn(maxWidth = 254.dp)
                                )
                                Text(
                                    text = "${
                                        when (section) {
                                            EditArtTypeSection.LEFT -> customTextLeft.value.length
                                            EditArtTypeSection.CENTER -> customTextCenter.value.length
                                            EditArtTypeSection.RIGHT -> customTextRight.value.length
                                        }
                                    } / $maximumCustomTextLength",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    },
                    text = stringResource(type.header),
                    subtext = when (type) {
                        EditArtTypeType.DISTANCE_MILES -> activitiesDistanceMetersSummed.value.meterToMilesStr()
                        EditArtTypeType.DISTANCE_KILOMETERS -> activitiesDistanceMetersSummed.value.meterToKilometerStr()
                        else -> null
                    }
                ) {
                    eventReceiver.onEvent(
                        TypeSelectionChanged(
                            section = section,
                            typeSelected = type
                        )
                    )
                }
            }
        }
    }
    Section(
        header = stringResource(R.string.edit_art_type_font_header),
        description = stringResource(R.string.edit_art_type_font_description)
    ) {
        FontType.values().forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = it == fontSelected.value,
                    onClick = { eventReceiver.onEvent(TypeFontChanged(changedTo = it)) }
                )
                Text(
                    text = stringResource(it.strRes),
                    fontFamily = FontFamily(Typeface.createFromAsset(
                        context.assets,
                        it.getAssetPath(
                            /** Provides a loud failure if missing regular font **/
                            it.fontWeightTypes.firstOrNull {
                                it == FontWeightType.REGULAR
                            } ?: error("Missing REGULAR font for font $it.")
                        )
                    ))
                )
            }
        }
    }
    if (fontSelected.value.fontWeightTypes.containsMultipleTypes) {
        Section(
            header = stringResource(R.string.edit_art_type_font_weight_header),
            description = stringResource(R.string.edit_art_type_font_weight_description)
        ) {
            fontSelected.value.fontWeightTypes.forEach {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButtonContentRow(
                        isSelected = fontWeightSelected.value == it,
                        text = stringResource(it.stringRes),
                        fontFamily = FontFamily(
                            Typeface.createFromAsset(
                                context.assets,
                                fontSelected.value.getAssetPath(it)
                            )
                        )
                    ) { eventReceiver.onEvent(TypeFontWeightChanged(changedTo = it)) }
                }
            }
        }
    }
    if (fontSelected.value.isItalic) {
        Section(
            header = stringResource(R.string.edit_art_type_font_italic_header),
            description = stringResource(R.string.edit_art_type_font_italic_description)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(checked = fontItalicized.value, onCheckedChange = {
                    eventReceiver.onEvent(TypeFontItalicChanged(changedTo = it))
                })
                Text(
                    text = stringResource(
                        if (fontItalicized.value) {
                            R.string.edit_art_type_font_italic_enabled
                        } else {
                            R.string.edit_art_type_font_italic_disabled
                        }
                    ),
                    fontFamily = FontFamily(
                        Typeface.createFromAsset(
                            context.assets,
                            fontSelected.value.getAssetPath(fontWeightSelected.value, fontItalicized.value)
                        )
                    )
                )
            }
        }
    }

    Section(
        header = stringResource(R.string.edit_art_type_size_header),
        description = stringResource(R.string.edit_art_type_size_description)
    ) {
        FontSizeType.values().forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButtonContentRow(
                    isSelected = fontSizeSelected.value == it,
                    text = stringResource(it.strRes)
                ) {
                    eventReceiver.onEvent(
                        TypeFontSizeChanged(
                            it
                        )
                    )
                }
            }
        }
    }
}

private fun Int.meterToMilesStr(): String = "${(this * 0.000621371192).roundToInt()} mi"

private fun Int.meterToKilometerStr(): String = "${(this / 1000f).roundToInt()} km"

private const val SINGLE_ITEM_SIZE = 1
private val List<FontWeightType>.containsMultipleTypes get() = size > SINGLE_ITEM_SIZE