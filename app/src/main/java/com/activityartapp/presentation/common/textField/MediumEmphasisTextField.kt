package com.activityartapp.presentation.common.textField

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy

@Composable
fun MediumEmphasisTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = {
            SubheadHeavy(text = text)
        },
        onValueChange = {

        }
    )
    /*
    onClick = onClick,
    shape = RoundedCornerShape(cornerRadiusDp),
    colors = ButtonDefaults.outlinedButtonColors(
        backgroundColor = colorResource(R.color.transparent)
    ),
    modifier = modifier
        .defaultMinSize(
            minHeight = size.getMinHeight()
        ),
    border = BorderStroke(
        width = strokeWidthDp,
        color = if (enabled) {
            colorResource(R.color.strava_orange)
        } else {
            colorResource(R.color.n30_silver)
        }
    ),
    enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.n80_asphalt),
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
        } else {
            val localDensity = LocalDensity.current
            var dividerWidth by remember { mutableStateOf(0.dp) }

            ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
                val textColor = enabled
                    .takeIf { it }
                    ?.let { colorResource(id = R.color.strava_orange) }
                label?.let {
                    SubheadHeavy(
                        text = it,
                        textColor = textColor,
                        modifier = Modifier.onGloballyPositioned {
                            dividerWidth = dividerWidth.coerceAtLeast(
                                localDensity.run { it.size.width.toDp() }
                            )
                        }
                    )
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                        color = textColor ?: colorResource(id = R.color.light_text_primary)
                    )
                }
                text?.let {
                    Subhead(
                        text = it,
                        textColor = textColor,
                        modifier = Modifier.onGloballyPositioned {
                            dividerWidth = dividerWidth.coerceAtLeast(
                                localDensity.run { it.size.width.toDp() }
                            )
                        }
                    )
                }
            }
        }
        imageVector?.let {
            Icon(imageVector = imageVector, contentDescription = contentDescription)
        }
    }

     */
}