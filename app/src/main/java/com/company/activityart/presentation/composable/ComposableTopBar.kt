package com.company.activityart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.ui.spacing
import com.company.activityart.presentation.ui.theme.StravaOrange

@Composable
fun ComposableTopBar(
    leftContent: @Composable (RowScope.() -> Unit)?,
    rightContent: @Composable (RowScope.() -> Unit)?
) {
    CompositionLocalProvider(
        LocalRippleTheme provides ClearRippleTheme
    ) {
        TopAppBar(
            backgroundColor = StravaOrange,
            modifier = Modifier.height(65.dp)
        ) {
            if (leftContent != null) leftContent()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (rightContent != null) rightContent()
            }
        }
    }
}

// https://stackoverflow.com/questions/69783654/how-to-disable-ripple-effect-on-navigation-bar-items-in-jetpack-compose-and-mate
// Important to remove the ripple effect on screen transition
object ClearRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}