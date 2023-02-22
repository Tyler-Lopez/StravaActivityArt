package com.activityartapp.util

import android.util.Size
import androidx.compose.ui.geometry.Offset
import com.activityartapp.util.enums.GradientAngleType
import com.activityartapp.util.enums.GradientAngleType.*

class GradientAngleUtils {
    fun getStartAndEndOffsets(
        gradientAngleType: GradientAngleType,
        size: Size
    ): Pair<Offset, Offset> {
        val width = size.width.toFloat()
        val height = size.height.toFloat()
        return when (gradientAngleType) {
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