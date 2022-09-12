package com.company.activityart.util.ext

import kotlin.math.abs


fun <T: Number> List<T>.closestValue(value: T): T =
    minBy { abs(value.toFloat() - it.toFloat())  }
