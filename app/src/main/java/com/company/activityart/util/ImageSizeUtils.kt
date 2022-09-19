package com.company.activityart.util

import android.util.Size

class ImageSizeUtils {

    companion object {}

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
        return Size(0, 0)
    }
}