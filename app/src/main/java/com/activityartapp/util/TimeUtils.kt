package com.activityartapp.util

import java.time.Instant
import java.time.Year
import java.time.YearMonth
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

class TimeUtils {

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
        private const val FIRST_HOUR_OF_DAY = 0
        private const val FIRST_MINUTE_OF_HOUR = 0
        private const val FIRST_SECOND_OF_MINUTE = 0
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val LAST_HOUR_OF_DAY = 23
        private const val LAST_MINUTE_OF_HOUR = 59
        private const val LAST_SECOND_OF_MINUTE = 59
        private const val LAST_DAY_OF_LAST_MONTH = 31

        private const val MONTHS_IN_YEAR = 12
    }

    /**
     * Returns a 0-indexed month corresponding to an ISO8601 String.
     */
    fun iso8601StringToMonth(value: String): Int {
        return Calendar.getInstance().run {
            time = Date.from(Instant.parse(value))
            get(Calendar.MONTH)
        }
    }

    /**
     * Returns a 0-indexed month corresponding to an ISO8601 String.
     */
    fun iso8601StringToYear(value: String): Int {
        return Calendar.getInstance().run {
            time = Date.from(Instant.parse(value))
            get(Calendar.YEAR)
        }
    }

    /**
     * Returns a year & 0-indexed month corresponding to an ISO8601 String.
     */
    fun iso8601StringToYearMonth(value: String): Pair<Int, Int> {
        return Calendar.getInstance().run {
            time = Date.from(Instant.parse(value))
            get(Calendar.YEAR) to get(Calendar.MONTH)
        }
    }

    fun iso8601StringToUnixSecond(value: String): Long {
        return Instant.parse(value).epochSecond
    }

    fun iso8601StringToUnixMillisecond(value: String): Long {
        return TimeUnit.SECONDS.toMillis(Instant.parse(value).epochSecond)
    }

    /**
     * Returns the first unix second before the given year month.
     *
     * @param month Zero-indexed month ranging from 0 to 11.
     */
    fun lastUnixSecondBeforeYearMonth(
        year: Int,
        month: Int
    ): Int {
        val adjYear = year.takeIf { month != FIRST_MONTH_OF_YEAR } ?: (year - 1)
        val adjMonth = if (month != FIRST_MONTH_OF_YEAR) {
            month - 1
        } else {
            LAST_MONTH_OF_YEAR
        }

        /** [GregorianCalendar] uses a 0-indexed [month] (0-11)
         * while [YearMonth] uses 1-indexed (1-12) **/
        val calendar = GregorianCalendar(
            adjYear,
            adjMonth,
            YearMonth.of(adjYear, (adjMonth + 1)).lengthOfMonth(),
            LAST_HOUR_OF_DAY,
            LAST_MINUTE_OF_HOUR,
            LAST_SECOND_OF_MINUTE
        )
        return MILLISECONDS.toSeconds(calendar.timeInMillis).toInt()
    }

    /**
     * Returns the last unix second corresponding to the given year month.
     */
    fun firstUnixSecondAfterYearMonth(
        year: Int,
        month: Int
    ): Int {
        val adjYear = year.takeIf { month != LAST_MONTH_OF_YEAR } ?: (year + 1)
        val adjMonth = if (month != LAST_MONTH_OF_YEAR) {
            month + 1
        } else {
            FIRST_MONTH_OF_YEAR
        }
        val calendar = GregorianCalendar(
            adjYear,
            adjMonth,
            FIRST_DAY_OF_MONTH,
            FIRST_HOUR_OF_DAY,
            FIRST_MINUTE_OF_HOUR,
            FIRST_SECOND_OF_MINUTE
        )
        return MILLISECONDS.toSeconds(calendar.timeInMillis).toInt()
    }

    /** Returns 1-indexed integer representing the last month of this year
     * or the current month if the year is the current year. */
    fun lastMonthInYearOrCurrentMonthIfNow(year: Int): Int {
        return if (year == Year.now().value) {
            YearMonth.now().month.value
        } else {
            MONTHS_IN_YEAR
        }
    }

    fun firstUnixMsOfYear(year: Int): Long {
        return GregorianCalendar(
            year,
            FIRST_MONTH_OF_YEAR,
            FIRST_DAY_OF_MONTH,
            FIRST_HOUR_OF_DAY,
            FIRST_MINUTE_OF_HOUR,
            FIRST_SECOND_OF_MINUTE
        ).timeInMillis
    }

    fun lastUnixMsOfYear(year: Int): Long {
        return GregorianCalendar(
            year,
            LAST_MONTH_OF_YEAR,
            LAST_DAY_OF_LAST_MONTH,
            LAST_HOUR_OF_DAY,
            LAST_MINUTE_OF_HOUR,
            LAST_SECOND_OF_MINUTE
        ).timeInMillis
    }
}