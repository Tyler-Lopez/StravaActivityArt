package com.company.activityart.util.ext

import kotlin.math.abs

fun IntRange.toFloatRange() = start.toFloat()..endInclusive.toFloat()
fun IntRange.closestValue(value: Int): Int = minBy { (abs(value.toFloat() - it.toFloat()))  }