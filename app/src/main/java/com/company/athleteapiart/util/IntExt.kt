package com.company.athleteapiart.util

import android.content.res.Resources

fun Int.pxToDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()