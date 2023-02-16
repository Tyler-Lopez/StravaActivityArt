package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

data class TextFieldSliderSpecification(
    val errorMessage: String?,
    val keyboardType: KeyboardType,
    val textFieldLabel: String,
    val textFieldValue: String,
    val sliderValue: Float,
    val sliderRange: ClosedFloatingPointRange<Float>,
    val onSliderChanged: (Float) -> Unit,
    val onTextFieldChanged: (String) -> Unit,
    val onTextFieldDone: (() -> Unit)
)

@Composable
fun TextFieldSliders(
    specifications: List<TextFieldSliderSpecification>
) {
    val focusManager = LocalFocusManager.current
    ColumnMediumSpacing(modifier = Modifier.padding(spacing.small)) {
        specifications.forEach { spec ->
            val interactionSource = remember { MutableInteractionSource() }
            ColumnSmallSpacing {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.small)
                ) {
                    // https://stackoverflow.com/questions/72886062/outlinedtextfield-doesnt-resize-to-a-minimumwidth-in-compose
                    OutlinedTextField(
                        value = spec.textFieldValue,
                        onValueChange = spec.onTextFieldChanged,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = spec.keyboardType,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.width(88.dp),
                        keyboardActions = KeyboardActions(onDone = {
                            spec.onTextFieldDone()
                            focusManager.clearFocus()
                        }),
                        label = {
                            Text(
                                text = spec.textFieldLabel,
                                style = MaterialTheme.typography.overline
                            )
                        },
                        singleLine = true,
                        interactionSource = interactionSource,
                    )
                    Slider(
                        value = spec.sliderValue,
                        onValueChange = spec.onSliderChanged,
                        valueRange = spec.sliderRange
                    )
                }
                spec.errorMessage?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }
    }
}