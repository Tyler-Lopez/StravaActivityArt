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
    NEWSREADER(
        fontName = R.string.font_newsreader,
        fileName = "newsreader_regular"
    ),
    NEWSREADER_LIGHT(
        fontName = R.string.font_newsreader_light,
        fileName = "newsreader_light"
    ),
    CINZEL(
        fontName = R.string.font_cinzel,
        fileName = "cinzel_regular"
    ),
    SATISFY(
        fontName = R.string.font_satisfy,
        fileName = "satisfy_regular"
    ),
    OSWALD(
        fontName = R.string.font_oswald,
        fileName = "oswald_regular"
    ),
    QUICKSAND_LIGHT(
        fontName = R.string.font_quicksand_bold,
        fileName = "quicksand_bold"
    ),
    QUICKSAND(
        fontName = R.string.font_quicksand_regular,
        fileName = "quicksand_regular"
    ),
    QUICKSAND_BOLD(
        fontName = R.string.font_quicksand_light,
        fileName = "quicksand_light"
    ),
    JOSEFIN_SANS_BOLD(
        fontName = R.string.font_josefin_sans_bold,
        fileName = "josefinsans_bold"
    ),
    JOSEFIN_SANS_BOLD_ITALIC(
        fontName = R.string.font_josefin_sans_bold_italic,
        fileName = "josefinsans_bolditalic"
    ),
    JOSEFIN_SANS_REGULAR(
        fontName = R.string.font_josefin_sans_regular,
        fileName = "josefinsans_regular"
    ),
    JOSEFIN_SANS_LIGHT(
        fontName = R.string.font_josefin_sans_light,
        fileName = "josefinsans_light"
    ),
    JOSEFIN_SANS_LIGHT_ITALIC(
        fontName = R.string.font_josefin_sans_light_italic,
        fileName = "josefinsans_lightitalic"
    );

    companion object {
        private const val ASSET_FONTS_FOLDER_PREFIX = "fonts/"
        private const val FONT_FILE_EXTENSION = ".ttf"
    }

    val assetFilepath: String
        get() = "$ASSET_FONTS_FOLDER_PREFIX$fileName$FONT_FILE_EXTENSION"
}