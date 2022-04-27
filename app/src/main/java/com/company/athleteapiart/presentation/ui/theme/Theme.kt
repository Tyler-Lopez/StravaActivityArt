package com.company.athleteapiart.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.company.athleteapiart.presentation.ui.LocalSpacing
import com.company.athleteapiart.presentation.ui.Spacing

private val DarkColorPalette = darkColors(
    primary = StravaOrange,
    primaryVariant = DarkerOrange,
    secondary = StravaOrange
)

private val LightColorPalette = lightColors(
    primary = StravaOrange,
    primaryVariant = DarkerOrange,
    secondary = StravaOrange,
    background = Color(242, 242, 242),

    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun AthleteApiArtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}