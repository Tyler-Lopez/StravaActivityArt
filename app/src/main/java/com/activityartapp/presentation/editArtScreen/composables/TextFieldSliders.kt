package com.activityartapp.presentation.editArtScreen.composables

import androidx.annotation.Px
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

data class TextFieldSliderSpecification(
    val enabled: Boolean,
    val errorMessage: String?,
    val keyboardType: KeyboardType,
    val textFieldLabel: String,
    val textFieldValue: String,
    val sliderValue: Float,
    val sliderRange: ClosedFloatingPointRange<Float>,
    val onSliderChanged: (Float) -> Unit,
    val onTextFieldChanged: (String) -> Unit
)

@OptIn(ExperimentalMaterialApi::class)
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
                    BasicTextField(
                        value = spec.textFieldValue,
                        onValueChange = spec.onTextFieldChanged,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = spec.keyboardType,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.width(88.dp),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        interactionSource = interactionSource,
                        decorationBox = {
                            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                                value = spec.textFieldValue,
                                visualTransformation = VisualTransformation.None,
                                innerTextField = it,
                                singleLine = true,
                                enabled = spec.enabled,
                                label = {
                                    Text(
                                        text = spec.textFieldLabel,
                                        style = MaterialTheme.typography.overline
                                    )
                                },
                                interactionSource = interactionSource,
                            )
                        }
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