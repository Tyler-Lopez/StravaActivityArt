package com.activityartapp.presentation.common.button

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
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy

@Composable
fun MediumEmphasisButtonLegacy(
    size: ButtonSize,
    modifier: Modifier = Modifier
        .defaultMinSize(minWidth = dimensionResource(id = R.dimen.button_min_width)),
    text: String? = null,
    label: String? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val cornerRadiusDp = dimensionResource(id = R.dimen.button_corner_radius)
    val strokeWidthDp = dimensionResource(id = R.dimen.button_stroke_width)

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadiusDp),
        modifier = modifier
            .defaultMinSize(
                minHeight = size.getMinHeight()
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

                label?.let {
                    SubheadHeavy(
                        text = it,
                        modifier = Modifier.onGloballyPositioned {
                            dividerWidth = dividerWidth.coerceAtLeast(
                                localDensity.run { it.size.width.toDp() }
                            )
                        }
                    )
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                    )
                }
                text?.let {
                    Subhead(
                        text = it,
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
}