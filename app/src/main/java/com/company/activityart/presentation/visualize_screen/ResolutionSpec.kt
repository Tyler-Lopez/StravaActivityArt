package com.company.activityart.presentation.visualize_screen

import kotlin.math.sqrt

enum class ResolutionSpec(
    val display: String,
    val resolution: Pair<Float, Float>
) {
    WIDE_DESKTOP("HD Desktop", Pair(1920f, 1080f)),
    FOUR_K_DESKTOP("4K Desktop", Pair(3840f, 2160f)),
    ULTRA_WIDE_DESKTOP("Wide Desktop", Pair(3440f, 1440f)),
    LINKEDIN_COVER("LinkedIn Cover", Pair(1584f, 396f)),
    FIVE_BY_SEVEN("5x7 Print", Pair(1500f, 2100f)),
    EIGHT_BY_TEN("8x10 Print", Pair(2400f, 3000f)),
    ELEVEN_BY_FOURTEEN("11x14 Print", Pair(3300f, 4200f)),
    TWELVE_BY_EIGHTEEN("12x18 Print", Pair(3328f, 4992f)),
    EIGHTEEN_BY_TWENTYFOUR("18x24 Print", Pair(2700f, 3600f)),
    TWENTYFOUR_BY_THIRTYSIX("24x36 Print", Pair(3600f, 5400f)),
    THIRTYSIX_BY_FOURTYEIGHT("36x48 Print", Pair(5400f, 7200f)),
    FOURTYEIGHT_BY_SEVENTYTWO("48x72 Print", Pair(7200f, 10800f)),
    FOURTYEIGHT_BY_ONEHUNDRED("48x100 Print", Pair(7200f, 15000f));

    val totalPixels = sqrt(resolution.first * resolution.second)
    val widthHeightRatio = resolution.first / resolution.second
}