package com.activityartapp.util.classes

import java.util.*

/** @param month 0-indexed
 *
 */
data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val PADDING_CHAR = '0'
        private const val HOUR_OF_DAY_FIRST = 0
        private const val HOUR_OF_DAY_LAST = 23
        private const val MINUTE_OF_HOUR_FIRST = 0
        private const val MINUTE_OF_HOUR_LAST = 59
        private const val SECOND_OF_MINUTE_FIRST = 0
        private const val SECOND_OF_MINUTE_LAST = 0

        fun fromUnixMs(ms: Long): YearMonthDay {
            return Calendar.getInstance().run {
                timeInMillis = ms
                YearMonthDay(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH),
                    get(Calendar.DAY_OF_MONTH)
                )
            }
        }
    }

    override fun toString(): String {
        val paddedMonth = "${month + 1}".padStart(
            MIN_LENGTH,
            PADDING_CHAR
        )
        val paddedDay = "$day".padStart(
            MIN_LENGTH,
            PADDING_CHAR
        )
        return "$paddedMonth / $paddedDay / ${year % 100}"
    }

    /**
     * @return Returns the UNIX millisecond corresponding to the [YearMonthDay]
     * when hour, minute, and second are not specified.
     */
    val unixMs: Long
        get() {
            return GregorianCalendar(
                year,
                month,
                day
            ).timeInMillis
        }

    /**
     * @return Returns explicitly the first UNIX millisecond corresponding to the
     * [YearMonthDay] according to [HOUR_OF_DAY_FIRST], [MINUTE_OF_HOUR_FIRST], and
     * [SECOND_OF_MINUTE_FIRST].
     */
    val unixMsFirst: Long
        get() {
            return GregorianCalendar(
                year,
                month,
                day,
                HOUR_OF_DAY_FIRST,
                MINUTE_OF_HOUR_FIRST,
                SECOND_OF_MINUTE_FIRST
            ).timeInMillis
        }

    /**
     * @return Returns explicitly the last UNIX millisecond corresponding to the
     * [YearMonthDay] according to [HOUR_OF_DAY_LAST], [MINUTE_OF_HOUR_LAST], and
     * [SECOND_OF_MINUTE_LAST].
     */
    val unixMsLast: Long
        get() {
            return GregorianCalendar(
                year,
                month,
                day,
                HOUR_OF_DAY_LAST,
                MINUTE_OF_HOUR_LAST,
                SECOND_OF_MINUTE_LAST
            ).timeInMillis
        }
}
