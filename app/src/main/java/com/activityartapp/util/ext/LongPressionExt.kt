package com.activityartapp.util.ext

import kotlin.math.abs

fun LongProgression.closestValue(value: Long) = minBy { abs(value - it) }
fun LongProgression.toFloatRange() = first.toFloat()..last.toFloat()