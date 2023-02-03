package com.activityartapp.presentation.common.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.activityartapp.R

@Composable
fun Button(
    emphasis: ButtonEmphasis,
    size: ButtonSize,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val content: @Composable RowScope.() -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(dimensionResource(id = R.dimen.loading_icon_size)),
                strokeWidth = dimensionResource(id = R.dimen.loading_icon_stroke_width)
            )
        } else {
            Text(text = text)
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