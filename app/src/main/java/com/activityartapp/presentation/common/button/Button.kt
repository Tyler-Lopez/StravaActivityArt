package com.activityartapp.presentation.common.button

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.activityartapp.R
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun Button(
    emphasis: ButtonEmphasis,
    size: ButtonSize,
    text: String,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    onClick: () -> Unit
) {
    val content: @Composable RowScope.() -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                color = if (enabled) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                },
                modifier = Modifier.size(dimensionResource(id = R.dimen.loading_icon_size)),
                strokeWidth = dimensionResource(id = R.dimen.loading_icon_stroke_width)
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = leadingIconContentDescription
                    )
                }
                labelText?.let {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(spacing.small)
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.overline
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.button
                        )
                    }
                } ?: Text(
                    text = text,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

    when (emphasis) {
        ButtonEmphasis.HIGH -> Button(
            content = content,
            enabled = enabled,
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(
                    minWidth = dimensionResource(id = R.dimen.button_min_width),
                    minHeight = size.getMinHeight()
                )
        )
        ButtonEmphasis.MEDIUM -> OutlinedButton(
            content = content,
            enabled = enabled,
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(
                    minWidth = dimensionResource(id = R.dimen.button_min_width),
                    minHeight = size.getMinHeight()
                )
        )
        ButtonEmphasis.LOW -> TextButton(
            content = content,
            enabled = enabled,
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(
                    minWidth = dimensionResource(id = R.dimen.button_min_width),
                    minHeight = size.getMinHeight()
                )
        )
    }
}