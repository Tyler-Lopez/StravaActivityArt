package com.activityartapp.util.ext

import kotlin.math.roundToInt

// Todo, this logic could be improved
val ClosedFloatingPointRange<Float>.integersInRange: Int
    get() {
        return (endInclusive.roundToInt() - start.roundToInt()) + 1
    }