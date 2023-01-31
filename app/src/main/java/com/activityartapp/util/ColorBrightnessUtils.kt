package com.activityartapp.util

import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

class ColorBrightnessUtils {

    companion object {
        private const val MAXIMUM_DARK_LUMINANCE = 0.5
    }

    fun selectPaintsRelativeToColor(
        @ColorInt color: Int,
        @ColorInt colorWhenDarkOne: Int,
        @ColorInt colorWhenDarkTwo: Int,
        @ColorInt colorWhenLightOne: Int,
        @ColorInt colorWhenLightTwo: Int
    ): Pair<Paint, Paint> {
        val paintOne: Paint
        val paintTwo: Paint

        if (isDark(color)) {
            paintOne = Paint().apply { this.color = colorWhenDarkOne }
            paintTwo = Paint().apply { this.color = colorWhenDarkTwo }
        } else {
            paintOne = Paint().apply { this.color = colorWhenLightOne }
            paintTwo = Paint().apply { this.color = colorWhenLightTwo }
        }

        return paintOne to paintTwo
    }

    private fun isDark(@ColorInt color: Int): Boolean =
        ColorUtils.calculateLuminance(color) <= MAXIMUM_DARK_LUMINANCE
}