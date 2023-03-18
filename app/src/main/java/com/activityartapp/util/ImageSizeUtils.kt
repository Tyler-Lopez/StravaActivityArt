package com.activityartapp.util

import androidx.compose.ui.geometry.Size

class ImageSizeUtils {

    /**
     * Provided an [actualSize] and a [maximumSize], returns a scaled-version
     * of [actualSize].
     *
     * @param actualSize The actual size of the image, which may be greater or
     * smaller than [maximumSize].
     * @param maximumSize The maximum size the image may occupy.
     *
     * @return Scaled version of [actualSize] which occupies the entire width
     * or height of [maximumSize] while maintaining its aspect ratio.
     */
    fun sizeToMaximumSize(
        actualSize: Size,
        maximumSize: Size
    ): Size {
        /** Determine width to height aspect ratio **/
        val widthHeightAspectRatio = actualSize.run {
            width / height
        }

        /** If the width of a theoretical size is scaled to the full
         * width of [maximumSize], determine if height satisfies that
         * which is also required by [maximumSize].
         *
         * If it does - return that scaling, else return scaling
         * with respect to height. */
        val heightWhenWidthScaled = maximumSize.width / widthHeightAspectRatio
        return if (heightWhenWidthScaled <= maximumSize.height) {
            Size(maximumSize.width, heightWhenWidthScaled)
        } else {
            val widthWhenHeightScaled = maximumSize.height * widthHeightAspectRatio
            Size(widthWhenHeightScaled, maximumSize.height)
        }
    }
}