package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class FontType(
    private val baseFileName: String,
    val fontWeightTypes: List<FontWeightType>,
    val isItalic: Boolean,
    @StringRes val strRes: Int,
) {
    JOSEFIN_SANS(
        baseFileName = "josefinsans",
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT,
            FontWeightType.THIN
        ),
        isItalic = true,
        strRes = R.string.font_josefinsans,
    ),
    JOSEFIN_SLAB(
        baseFileName = "josefinslab",
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT,
            FontWeightType.THIN
        ),
        isItalic = true,
        strRes = R.string.font_josefinslab,
    ),
    QUICKSAND(
        baseFileName = "quicksand",
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT
        ),
        isItalic = false,
        strRes = R.string.font_quicksand
    ),
    MERRIWEATHER(
        baseFileName = "merriweather",
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT
        ),
        isItalic = true,
        strRes = R.string.font_merriweather
    ),
    LOBSTER_TWO(
        baseFileName = "lobster_two",
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.REGULAR
        ),
        isItalic = true,
        strRes = R.string.font_lobster_two
    ),
    OSWALD(
        baseFileName = "oswald",
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT
        ),
        isItalic = false,
        strRes = R.string.font_oswald
    ),
    PLAYFAIR_DISPLAY(
        baseFileName = "playfair_display",
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.EXTRA_BOLD,
            FontWeightType.BOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR
        ),
        isItalic = true,
        strRes = R.string.font_playfair_display
    ),
    PLAYFAIR_DISPLAY_SC(
        baseFileName = "playfair_display_sc",
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.REGULAR
        ),
        isItalic = true,
        strRes = R.string.font_playfair_display_sc
    ),
    ROBOTO(
        baseFileName = "roboto",
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.THIN
        ),
        isItalic = true,
        strRes = R.string.font_roboto
    ),
    ALEGREYA(
        baseFileName = "alegreya",
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.EXTRA_BOLD,
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR
        ),
        isItalic = true,
        strRes = R.string.font_alegreya
    );

    companion object {
        private const val ASSET_FONTS_FOLDER_PREFIX = "fonts/"
        private const val FOLDER_DIVIDER = "/"
        private const val FILE_DIVIDER = "_"
        private const val FONT_FILE_EXTENSION = ".ttf"
    }

    /**
     * Returns path to the font file as located in the assets folder.
     * Builds a string defining which combination of font, font weight, and italicizes to use.
     **/
    fun getAssetPath(
        fontWeightType: FontWeightType = FontWeightType.REGULAR,
        italicized: Boolean = false
    ): String = buildString {
        append(ASSET_FONTS_FOLDER_PREFIX)
        append(baseFileName)
        append(FOLDER_DIVIDER)
        append(
            when (fontWeightType) {
                FontWeightType.BLACK -> "black"
                FontWeightType.EXTRA_BOLD -> "extrabold"
                FontWeightType.BOLD -> "bold"
                FontWeightType.SEMIBOLD -> "semibold"
                FontWeightType.MEDIUM -> "medium"
                FontWeightType.REGULAR -> "regular"
                FontWeightType.LIGHT -> "light"
                FontWeightType.EXTRA_LIGHT -> "extralight"
                FontWeightType.THIN -> "thin"
            }
        )
        if (italicized) {
            append(FILE_DIVIDER)
            append("italic")
        }
        append(FONT_FILE_EXTENSION)
    }
}