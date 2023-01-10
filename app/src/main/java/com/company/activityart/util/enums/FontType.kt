package com.company.activityart.util.enums

import androidx.annotation.StringRes
import com.company.activityart.R

enum class FontType(
    private val baseFileName: String,
    @StringRes val strRes: Int,
    val fontStyleTypes: List<FontStyleType> = listOf(),
    val fontWeightTypes: List<FontWeightType> = listOf(),
) {
    JOSEFIN_SANS(
        baseFileName = "josefinsans",
        strRes = R.string.font_josefinsans,
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT,
            FontWeightType.THIN
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    JOSEFIN_SLAB(
        baseFileName = "josefinslab",
        strRes = R.string.font_josefinslab,
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT,
            FontWeightType.THIN
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    QUICKSAND(
        baseFileName = "quicksand",
        strRes = R.string.font_quicksand,
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    MERRIWEATHER(
        baseFileName = "merriweather",
        strRes = R.string.font_merriweather,
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    LOBSTER_TWO(
        baseFileName = "lobster_two",
        strRes = R.string.font_lobster_two,
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.REGULAR
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    OSWALD(
        baseFileName = "oswald",
        strRes = R.string.font_oswald,
        fontWeightTypes = listOf(
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.LIGHT,
            FontWeightType.EXTRA_LIGHT
        )
    ),
    PLAYFAIR_DISPLAY(
        baseFileName = "playfair_display",
        strRes = R.string.font_playfair_display,
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.EXTRA_BOLD,
            FontWeightType.BOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    PLAYFAIR_DISPLAY_SC(
        baseFileName = "playfair_display_sc",
        strRes = R.string.font_playfair_display_sc,
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.REGULAR
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC
        )
    ),
    ROBOTO(
        baseFileName = "roboto",
        strRes = R.string.font_roboto,
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.BOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR,
            FontWeightType.THIN
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
    ),
    ALEGREYA(
        baseFileName = "alegreya",
        strRes = R.string.font_alegreya,
        fontWeightTypes = listOf(
            FontWeightType.BLACK,
            FontWeightType.EXTRA_BOLD,
            FontWeightType.BOLD,
            FontWeightType.SEMIBOLD,
            FontWeightType.MEDIUM,
            FontWeightType.REGULAR
        ),
        fontStyleTypes = listOf(
            FontStyleType.ITALIC,
            FontStyleType.CAPITALIZE
        )
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