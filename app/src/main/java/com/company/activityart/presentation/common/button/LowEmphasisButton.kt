package com.company.activityart.presentation.common.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.company.activityart.R


@Composable
fun LowEmphasisButton(
    size: ButtonSize,
    modifier: Modifier = Modifier
        .defaultMinSize(minWidth = dimensionResource(id = R.dimen.button_min_width)),
    text: String? = null,
    imageVector: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val cornerRadiusDp = dimensionResource(id = R.dimen.button_corner_radius)

    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadiusDp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = colorResource(R.color.transparent)
        ),
        modifier = modifier
            .defaultMinSize(
                minWidth = Dp.Unspecified,
                minHeight = size.getMinHeight()
            )
            .indication(
                remember { MutableInteractionSource() },
                rememberRipple(color = Color.Black)
            ),
        enabled = enabled
    ) {
        text?.let {
            Text(
                text = it,
                color = if (enabled) {
                    colorResource(id = R.color.strava_orange)
                } else {
                    colorResource(id = R.color.n80_asphalt)
                },
                style = MaterialTheme.typography.button
            )
        }
        imageVector?.let {
            Icon(imageVector = imageVector, contentDescription = null)
        }
    }
}