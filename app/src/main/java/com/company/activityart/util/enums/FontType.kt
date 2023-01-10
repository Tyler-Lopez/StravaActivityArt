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