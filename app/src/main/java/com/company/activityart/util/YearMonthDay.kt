package com.company.activityart.util

import androidx.annotation.StringRes
import com.company.activityart.R

data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {

    companion object {
        private const val PADDING_LENGTH = 2
        private const val PADDING_DIGIT = '0'
    }

    val monthStringResource: Int
        @StringRes get() = when (month) {
            1 -> R.string.month_1_short
            2 -> R.string.month_2_short
            3 -> R.string.month_3_short
            4 -> R.string.month_4_short
            5 -> R.string.month_5_short
            6 -> R.string.month_6_short
            7 -> R.string.month_7_short
            8 -> R.string.month_8_short
            9 -> R.string.month_9_short
            10 -> R.string.month_10_short
            11 -> R.string.month_11_short
            12 -> R.string.month_12_short
            else -> 0 // Todo str resource
        }

    val paddedDay: String
        get() = day.toString().padStart(
            PADDING_LENGTH,
            PADDING_DIGIT
        )
}