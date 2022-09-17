package com.company.activityart.util

import com.company.activityart.util.classes.YearMonthDay
import java.time.*
import java.util.*
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

    /**
     * Provided a UNIX seconds timestamp, returns the corresponding year and month
     * which it represents.
     *
     * Todo, currently assumes UTC TimeZone. Must adjust to pass in actual from ISO8601.
     *
     * @param seconds A time in UNIX seconds.
     *
     * @return A pair of the year to 1-index month (1-12) representing when this UNIX
     * second occurred.
     */
    fun unixSecondToYearMonthDay(seconds: Long): YearMonthDay {
       return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC).run {
           YearMonthDay(year, month.value, dayOfMonth)
       }
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
}
/*
class TimeUtils {
    companion object {
        private const val DATE_TIME_FORMAT = "dd MM uuu HH mm"

        fun parseIso8601(string: String): Map<String, Int> {
            val instant = Instant.parse(string)
            val dateArr = LocalDateTime
                .ofInstant(instant, ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("dd MM uuuu HH mm"))
                .split(" ")
            return mapOf(
                "day" to dateArr[0].toInt(),
                "month" to dateArr[1].toInt(),
                "year" to dateArr[2].toInt(),
                "hour" to dateArr[3].toInt(),
                "minute" to dateArr[4].toInt()
            )
        }

        fun monthIntToString(int: Int) = when (int) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "???"
        }

        fun monthStringToInt(string: String) = when (string) {
            "JAN" -> 1
            "FEB" -> 2
            "MAR" -> 3
            "APR" -> 4
            "MAY" -> 5
            "JUN" -> 6
            "JUL" -> 7
            "AUG" -> 8
            "SEP" -> 9
            "OCT" -> 10
            "NOV" -> 11
            "DEC" -> 12
            else -> -1
        }

        fun yearsAvailable() = FIRST_YEAR..LocalDateTime.now().year

        fun timeToString(input: Int): String {
            val hours = input / 3600
            val minutes = (input % 3600) / 60
            val seconds = (input % 3600) % 60

            val toReturn = StringBuilder()
            if (hours != 0) toReturn.append("${hours}h ")
            if (minutes != 0 || hours != 0) toReturn.append("${minutes}m ")
            if (hours == 0) toReturn.append("${seconds}s")
            return toReturn.toString()
        }

        fun accessTokenExpired(time: Int): Boolean {
            val now = (GregorianCalendar().timeInMillis / 1000).toInt()
            return (now - time >= 20000)
        }
    }
}




 */