package com.company.athleteapiart.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.company.athleteapiart.util.Constants.FIRST_YEAR
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

class TimeUtils {
    companion object {
        fun yearsAvailable() = FIRST_YEAR..LocalDateTime.now().year
    }
}

@SuppressLint("NewApi")
fun String.formatIso8601(
    delimiter: Char = '-',
    showDay: Boolean = true,
    showMonth: Boolean = true,
    showYear: Boolean = true,
    ascending: Boolean = false
): String {
    val instant = Instant.parse(this)
    val day = if (showDay) "dd" else ""
    val month = if (showMonth) "MM" else ""
    val year = if (showYear) "uuuu" else ""
    return LocalDateTime
        .ofInstant(instant, ZoneOffset.UTC)
        .format(
            DateTimeFormatter.ofPattern(
                if (ascending)
                    "$day $month $year"
                else "$month $day $year"
            )
        ).trim().replace(' ', delimiter)
}