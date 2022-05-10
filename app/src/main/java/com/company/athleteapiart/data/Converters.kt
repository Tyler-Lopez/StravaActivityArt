package com.company.athleteapiart.data

import androidx.room.TypeConverter


class Converters {


    @TypeConverter
    fun fromYearsMonthCached(yearsMonthCached: Map<Int, Int>): String {
        return buildString {
            for (year in yearsMonthCached.keys) {
                // 2022:
                append(year).append(YEAR_DELIMITER)
                append(yearsMonthCached[year]).append(MONTH_DELIMITER)
            }
            println("HERE WE ARE DECODING ${this.toString()}")
        }
    }

    @TypeConverter
    fun toYearsMonthCached(raw: String): Map<Int, Int> {
        println("HERE $raw")
        val yearsArr =
            raw.split(Regex(pattern = "$YEAR_DELIMITER([0-9]$MONTH_DELIMITER)+|$YEAR_DELIMITER([0-9][0-9]$MONTH_DELIMITER)+"))
                .filter { it.isNotEmpty() } // 2001, 2002, 2003
        val months =
            raw.split(regex = Regex(pattern = "[0-9]{4}$YEAR_DELIMITER"))
                .filter { it.isNotEmpty() } // 0_1_2_3, 0_1_2, 0_1

        println(months)
        val toReturn = mutableMapOf<Int, Int>()

        for (i in 0..yearsArr.lastIndex) {
            val year = yearsArr[i]
            val monthsRead: Int = months[i].split(
                MONTH_DELIMITER
            )[0].toInt()
            toReturn[year.toInt()] = monthsRead
        }
        println("HERE WE ARE RETURNING ${toReturn.size} has that size $toReturn")
       return toReturn
    }

    companion object {
        private const val YEAR_DELIMITER = ':'
        private const val MONTH_DELIMITER = '-'

    }
}