package com.activityartapp.presentation.editArtScreen.subscreens.resize

import com.activityartapp.R
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.presentation.editArtScreen.Resolution

class ResolutionListFactoryImpl : ResolutionListFactory {

    companion object {
        private const val SIZE_CUSTOM_INITIAL_WIDTH_PX = 1920f
        private const val SIZE_CUSTOM_INITIAL_HEIGHT_PX = 1080f
        private const val SIZE_CUSTOM_MAXIMUM_PX = 12000f
        private const val SIZE_CUSTOM_MINIMUM_PX = 100f
    }

    override fun create(): MutableList<Resolution> {
        return mutableListOf(
            Resolution.ComputerResolution(
                R.string.edit_art_resize_option_computer_wallpaper,
                1920f,
                1080f
            ),
            Resolution.ComputerResolution(
                R.string.edit_art_resize_option_linkedin_banner,
                1584f,
                396f
            ),
            Resolution.PrintResolution(2400f, 2400f, 8, 8),
            Resolution.PrintResolution(2400f, 3000f, 8, 10),
            Resolution.PrintResolution(2400f, 3600f, 8, 12),
            Resolution.PrintResolution(3000f, 6000f, 10, 20),
            Resolution.PrintResolution(3300f, 4200f, 11, 14),
            Resolution.PrintResolution(3600f, 5400f, 12, 18),
            Resolution.PrintResolution(3600f, 7200f, 12, 24),
            Resolution.PrintResolution(3600f, 10800f, 12, 36),
            Resolution.PrintResolution(4800f, 4800f, 16, 16),
            Resolution.PrintResolution(4800f, 6000f, 16, 20),
            Resolution.PrintResolution(4800f, 7200f, 16, 24),
            Resolution.PrintResolution(6000f, 6000f, 20, 20),
            Resolution.PrintResolution(6000f, 9000f, 20, 30),
            Resolution.PrintResolution(6000f, 12000f, 20, 40),
            Resolution.CustomResolution(
                sizeWidthPx = SIZE_CUSTOM_INITIAL_WIDTH_PX,
                sizeHeightPx = SIZE_CUSTOM_INITIAL_HEIGHT_PX,
                sizeMaximumPx = SIZE_CUSTOM_MAXIMUM_PX,
                sizeMinimumPx = SIZE_CUSTOM_MINIMUM_PX
            )
        )
    }
}
