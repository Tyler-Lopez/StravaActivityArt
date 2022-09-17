package com.company.activityart.util.classes

import com.company.activityart.domain.use_case.activities.GetActivitiesByYearMonthFromLocalUseCase
import com.company.activityart.util.TimeUtils
import java.time.YearMonth
import java.util.*

data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val PADDING_CHAR = '0'
    }
    
    override fun toString(): String {
        val paddedMonth = "$month".padStart(
            MIN_LENGTH,
            PADDING_CHAR
        )
        val paddedDay = "$day".padStart(
            MIN_LENGTH,
            PADDING_CHAR
        )
        return "$month / $day / $year"
    }

    val unixMilliseconds: Long
        get() {
           return GregorianCalendar(
                year,
                month,
                day
            ).timeInMillis
        }
}
