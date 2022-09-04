package com.company.activityart.presentation.common.button

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.company.activityart.R

@Composable
fun HighEmphasisButton(
    size: ButtonSize,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val cornerRadiusDp = dimensionResource(id = R.dimen.button_corner_radius)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadiusDp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (enabled) {
                colorResource(R.color.strava_orange)
            } else {
                colorResource(R.color.n30_silver)
            }
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
                    colorResource(id = R.color.white)
                } else {
                    colorResource(id = R.color.n80_asphalt)
                },
                style = MaterialTheme.typography.button
            )
        }
    }
}