package com.activityartapp.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.activityartapp.util.enums.AngleType
import com.activityartapp.util.enums.AngleType.*

class GradientAngleUtils {
    fun getStartAndEndOffsets(
        angleType: AngleType,
        size: Size
    ): Pair<Offset, Offset> {
        val width = size.width
        val height = size.height
        return when (angleType) {
            CW0 -> Offset.Zero to Offset(width, 0f)
            CW45 -> Offset.Zero to Offset(width, height)
            CW90 -> Offset.Zero to Offset(0f, height)
            CW135 -> Offset(width, 0f) to Offset(0f, height)
            CW180 -> Offset(width, 0f) to Offset.Zero
            CW225 -> Offset(width, height) to Offset.Zero
            CW270 -> Offset(0f, height) to Offset.Zero
            CW315 -> Offset(0f, height) to Offset(width, 0f)
        }
    }
}