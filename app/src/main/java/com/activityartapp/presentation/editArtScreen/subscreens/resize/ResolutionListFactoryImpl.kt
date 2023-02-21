package com.activityartapp.presentation.editArtScreen.subscreens.resize

import com.activityartapp.R
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.presentation.editArtScreen.Resolution

class ResolutionListFactoryImpl : ResolutionListFactory {

    companion object {
        private const val SIZE_CUSTOM_INITIAL_WIDTH_PX = 1920
        private const val SIZE_CUSTOM_INITIAL_HEIGHT_PX = 1080
        private const val SIZE_CUSTOM_MAXIMUM_PX = 12000
        private const val SIZE_CUSTOM_MINIMUM_PX = 100
    }

    override fun create(): MutableList<Resolution> {
        return mutableListOf(
            Resolution.ComputerResolution(
                R.string.edit_art_resize_option_computer_wallpaper,
                1920,
                1080
            ),
            Resolution.ComputerResolution(
                R.string.edit_art_resize_option_linkedin_banner,
                1584,
                396
            ),
            Resolution.PrintResolution(2400, 2400, 8, 8),
            Resolution.PrintResolution(2400, 3000, 8, 10),
            Resolution.PrintResolution(2400, 3600, 8, 12),
            Resolution.PrintResolution(3000, 6000, 10, 20),
            Resolution.PrintResolution(3300, 4200, 11, 14),
            Resolution.PrintResolution(3600, 5400, 12, 18),
            Resolution.PrintResolution(3600, 7200, 12, 24),
            Resolution.PrintResolution(3600, 10800, 12, 36),
            Resolution.PrintResolution(4800, 4800, 16, 16),
            Resolution.PrintResolution(4800, 6000, 16, 20),
            Resolution.PrintResolution(4800, 7200, 16, 24),
            Resolution.PrintResolution(6000, 6000, 20, 20),
            Resolution.PrintResolution(6000, 9000, 20, 30),
            Resolution.PrintResolution(6000, 12000, 20, 40),
            Resolution.CustomResolution(
                widthPx = SIZE_CUSTOM_INITIAL_WIDTH_PX,
                heightPx = SIZE_CUSTOM_INITIAL_HEIGHT_PX,
                sizeMaximumPx = SIZE_CUSTOM_MAXIMUM_PX,
                sizeMinimumPx = SIZE_CUSTOM_MINIMUM_PX
            )
        )
    }
}
