package com.company.athleteapiart.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["athleteId"])
data class AthleteEntity(
    val athleteId: Long,
    val userName: String,
    // This is important as Strava does not want you caching data for more than 3 days
    val receivedOn: Int,
    val profilePictureMedium: String,
    val profilePictureLarge: String,
    val firstName: String,
    val lastName: String,
    // This is to tell us what we have and have not yet mapped to Room
    val yearMonthsCached: Map<Int, Int>
) {
    // Invoked privately
    fun lastCachedMonth(year: Int): Int {
        println("Here attempting to get $year from $yearMonthsCached")
        for (year in yearMonthsCached.keys)
            println("year contained in here is $year")
        return yearMonthsCached.getOrDefault(year, -1)
    }
}
