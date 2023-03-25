package com.activityartapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.activityartapp.R

@Composable
fun AthleteApiArtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = colorResource(R.color.primary_color_dark),
            primaryVariant = colorResource(R.color.primary_dark_color_dark),
            secondary = colorResource(R.color.secondary_color_dark),
            secondaryVariant = colorResource(R.color.primary_dark_color_dark),
            background = colorResource(R.color.background_color_dark),
            surface = colorResource(R.color.surface_color_dark),
            error = colorResource(R.color.error_color_dark),
            onPrimary = colorResource(R.color.on_primary_color_dark),
            onSecondary = colorResource(R.color.on_secondary_color_dark),
            onBackground = colorResource(R.color.on_background_color_dark),
            onSurface = colorResource(R.color.on_surface_color_dark),
            onError = colorResource(R.color.on_error_color_dark),
        )
    } else {
        lightColors(
            primary = colorResource(R.color.primary_color),
            primaryVariant = colorResource(R.color.primary_dark_color),
            secondary = colorResource(R.color.secondary_color),
            secondaryVariant = colorResource(R.color.primary_dark_color),
            background = colorResource(R.color.background_color),
            surface = colorResource(R.color.surface_color),
            error = colorResource(R.color.error_color),
            onPrimary = colorResource(R.color.on_primary_color),
            onSecondary = colorResource(R.color.on_secondary_color),
            onBackground = colorResource(R.color.on_background_color),
            onSurface = colorResource(R.color.on_surface_color),
            onError = colorResource(R.color.on_error_color),
        )
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography(
                defaultFontFamily = FontFamily(
                    Font(R.font.roboto_black, FontWeight.Black),
                    Font(R.font.roboto_bold, FontWeight.Bold),
                    Font(R.font.roboto_medium, FontWeight.Medium),
                    Font(R.font.roboto_regular, FontWeight.Normal),
                    Font(R.font.roboto_light, FontWeight.Light),
                    Font(R.font.roboto_blackitalic, FontWeight.Black, style = FontStyle.Italic),
                    Font(R.font.roboto_bolditalic, FontWeight.Bold, style = FontStyle.Italic),
                    Font(R.font.roboto_mediumitalic, FontWeight.Medium, style = FontStyle.Italic),
                    Font(R.font.roboto_italic, FontWeight.Normal, style = FontStyle.Italic),
                    Font(R.font.roboto_lightitalic, FontWeight.Light, style = FontStyle.Italic)
                )
            ),
            shapes = Shapes,
            content = content
        )
    }
}