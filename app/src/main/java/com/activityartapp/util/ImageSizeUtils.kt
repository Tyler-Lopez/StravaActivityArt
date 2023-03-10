package com.activityartapp.util

import android.util.Size
import androidx.compose.ui.geometry.Offset

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
            width.toFloat() / height.toFloat()
        }

        /** If the width of a theoretical size is scaled to the full
         * width of [maximumSize], determine if height satisfies that
         * which is also required by [maximumSize].
         *
         * If it does - return that scaling, else return scaling
         * with respect to height. */
        val heightWhenWidthScaled = maximumSize.width / widthHeightAspectRatio
        return if (heightWhenWidthScaled <= maximumSize.height) {
            Size(maximumSize.width, heightWhenWidthScaled.toInt())
        } else {
            val widthWhenHeightScaled = maximumSize.height * widthHeightAspectRatio
            Size(widthWhenHeightScaled.toInt(), maximumSize.height)
        }
    }

    /**
     * Given an image within a container, returns the [Offset] necessary to center
     * the image within its container.
     *
     * Assumes the image offset with [Offset.Zero] is left-aligned.
     */
    fun offsetToCenterImageInContainer(
        container: Size,
        image: Size
    ): Offset {
        return Offset(
            (image.width.toFloat() - container.width) / 2F,
            (image.height.toFloat() - container.height) / 2F
        )
    }
}