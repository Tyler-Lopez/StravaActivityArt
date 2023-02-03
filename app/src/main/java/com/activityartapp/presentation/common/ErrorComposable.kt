package com.activityartapp.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButtonLegacy
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButtonLegacy
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.TitleTwo

@Composable
fun ColumnScope.ErrorScreen(
    header: String,
    description: String,
    prompt: String,
    retrying: Boolean = false,
    onContinueClicked: (() -> Unit)? = null,
    onReconnectStravaClicked: (() -> Unit)? = null,
    onRetryClicked: (() -> Unit)? = null,
    onReturnClicked: (() -> Unit)? = null
) {
    val buttonWidth = dimensionResource(id = R.dimen.button_min_width)
    var minButtonWidth: Dp by remember { mutableStateOf(buttonWidth) }
    val localDensity = LocalDensity.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            tint = colorResource(R.color.n90_coal),
            contentDescription = stringResource(R.string.error_warning_content_description)
        )
        TitleTwo(text = header)
    }
    CardColumn(modifier = Modifier
        .onGloballyPositioned {
            minButtonWidth = minButtonWidth.coerceAtLeast(localDensity.run { it.size.width.toDp() })
        }) {
        Body(text = description, textAlign = TextAlign.Center)
        Body(text = prompt, textAlign = TextAlign.Center)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        var hasUsedHighEmphasisButton = false
        var hasUsedMediumEmphasisButton = false

        onReconnectStravaClicked?.let {
            buttonWithEmphasis(
                enabled = !retrying,
                isLoading = false,
                text = stringResource(R.string.error_reconnect_button),
                minWidth = minButtonWidth,
                hasUsedHighEmphasisButton = hasUsedHighEmphasisButton,
                hasUsedMediumEmphasisButton = hasUsedMediumEmphasisButton,
                onClick = it
            ).apply {
                when (this) {
                    ButtonEmphasisType.HIGH -> hasUsedHighEmphasisButton = true
                    ButtonEmphasisType.MEDIUM -> hasUsedMediumEmphasisButton = true
                    else -> {}
                }
            }
        }

        onContinueClicked?.let {
            buttonWithEmphasis(
                enabled = !retrying,
                isLoading = false,
                text = stringResource(R.string.error_continue_button),
                minWidth = minButtonWidth,
                hasUsedHighEmphasisButton = hasUsedHighEmphasisButton,
                hasUsedMediumEmphasisButton = hasUsedMediumEmphasisButton,
                onClick = it
            ).apply {
                when (this) {
                    ButtonEmphasisType.HIGH -> hasUsedHighEmphasisButton = true
                    ButtonEmphasisType.MEDIUM -> hasUsedMediumEmphasisButton = true
                    else -> {}
                }
            }
        }
        onRetryClicked?.let {
            buttonWithEmphasis(
                enabled = !retrying,
                isLoading = retrying,
                text = stringResource(R.string.error_retry_button),
                minWidth = minButtonWidth,
                hasUsedHighEmphasisButton = hasUsedHighEmphasisButton,
                hasUsedMediumEmphasisButton = hasUsedMediumEmphasisButton,
                onClick = it
            ).apply {
                when (this) {
                    ButtonEmphasisType.HIGH -> hasUsedHighEmphasisButton = true
                    ButtonEmphasisType.MEDIUM -> hasUsedMediumEmphasisButton = true
                    else -> {}
                }
            }
        }
        onReturnClicked?.let {
            buttonWithEmphasis(
                enabled = !retrying,
                isLoading = false,
                text = stringResource(R.string.error_return_button),
                minWidth = minButtonWidth,
                hasUsedHighEmphasisButton = hasUsedHighEmphasisButton,
                hasUsedMediumEmphasisButton = hasUsedMediumEmphasisButton,
                onClick = it
            ).apply {
                when (this) {
                    ButtonEmphasisType.HIGH -> hasUsedHighEmphasisButton = true
                    ButtonEmphasisType.MEDIUM -> hasUsedMediumEmphasisButton = true
                    else -> {}
                }
            }
        }
    }
}

private enum class ButtonEmphasisType {
    LOW, MEDIUM, HIGH
}

@Composable
private fun buttonWithEmphasis(
    enabled: Boolean,
    isLoading: Boolean,
    text: String,
    minWidth: Dp,
    hasUsedHighEmphasisButton: Boolean,
    hasUsedMediumEmphasisButton: Boolean,
    onClick: () -> Unit
): ButtonEmphasisType {

    val emphasis = when {
        !hasUsedHighEmphasisButton -> ButtonEmphasisType.HIGH
        !hasUsedMediumEmphasisButton -> ButtonEmphasisType.MEDIUM
        else -> ButtonEmphasisType.LOW
    }

    when (emphasis) {
        ButtonEmphasisType.HIGH -> HighEmphasisButtonLegacy(
            size = ButtonSize.LARGE,
            enabled = enabled,
            isLoading = isLoading,
            modifier = Modifier.defaultMinSize(minWidth = minWidth),
            text = text,
            onClick = onClick
        )
        ButtonEmphasisType.MEDIUM -> MediumEmphasisButtonLegacy(
            size = ButtonSize.LARGE,
            enabled = enabled,
            isLoading = isLoading,
            modifier = Modifier.defaultMinSize(minWidth = minWidth),
            text = text,
            onClick = onClick
        )
        ButtonEmphasisType.LOW -> LowEmphasisButton(
            size = ButtonSize.LARGE,
            enabled = enabled,
            modifier = Modifier.defaultMinSize(minWidth = minWidth),
            text = text,
            onClick = onClick
        )
    }
    return emphasis
}