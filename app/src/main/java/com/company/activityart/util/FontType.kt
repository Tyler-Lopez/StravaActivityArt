package com.company.activityart.util

import androidx.annotation.StringRes
import com.company.activityart.R

enum class FontType(
    @StringRes val fontName: Int,
    private val fileName: String
) {
    BEBASNEUE(
        fontName = R.string.font_bebasneue,
        fileName = "bebasneue_regular"
    ),
    ROBOTO(
        fontName = R.string.font_roboto,
        fileName = "roboto_regular"
    ),
    ROBOTO_CONDENSED(
        fontName = R.string.font_roboto_condensed,
        fileName = "robotocondensed_regular"
    ),
    LOBSTER(
        fontName = R.string.font_lobster,
        fileName = "lobster_regular"
    ),
    LOBSTER_TWO(
        fontName = R.string.font_lobster_two,
        fileName = "lobstertwo_regular"
    ),
    OSWALD(
        fontName = R.string.font_oswald,
        fileName = "oswald_regular"
    );

    companion object {
        private const val ASSET_FONTS_FOLDER_PREFIX = "fonts/"
        private const val FONT_FILE_EXTENSION = ".ttf"
    }

    val assetFilepath: String
        get() = "$ASSET_FONTS_FOLDER_PREFIX$fileName$FONT_FILE_EXTENSION"


}