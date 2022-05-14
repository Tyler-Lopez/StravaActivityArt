package com.company.athleteapiart.util

import com.company.athleteapiart.util.Constants.FIRST_YEAR
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class TimeUtils {

    companion object {

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
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
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


