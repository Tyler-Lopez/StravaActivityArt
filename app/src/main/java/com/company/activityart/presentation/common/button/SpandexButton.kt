package com.company.activityart.presentation.common.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.company.activityart.R

@Composable
fun SpandexButton(
    buttonStyle: ButtonStyle,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String? = null,
) {
    val cornerRadiusDp = dimensionResource(id = R.dimen.button_corner_radius)
    val borderStrokeDp = dimensionResource(id = R.dimen.button_stroke_width)

    buttonStyle.apply {
        OutlinedButton(
            border = strokeColorEnabled?.let { sce ->
                strokeColorDisabled?.let { scd ->
                    BorderStroke(
                        width = borderStrokeDp,
                        color = if (enabled) sce else scd
                    )
                }
            },
            onClick = onClick,
            shape = RoundedCornerShape(cornerRadiusDp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = if (enabled) {
                    backgroundColorEnabled
                } else {
                    backgroundColorDisabled
                }
            ),
            modifier = modifier
                .defaultMinSize(
                    minWidth = Dp.Unspecified,
                    minHeight = size.getMinHeight()
                ),
            enabled = enabled
        ) {
            text?.let {
                Text(
                    text = it,
                    color = if (enabled) {
                        textColorEnabled
                    } else {
                        textColorDisabled
                    },
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}
