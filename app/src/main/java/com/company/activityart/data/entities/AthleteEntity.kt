package com.company.activityart.data.entities

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
    val yearMonthsCached: Map<Int, Int>,
    val gears: Map<String, String>
) {

    fun lastCachedMonth(year: Int): Int = yearMonthsCached.getOrDefault(year, -1)

    fun withNewCaches(cache: Map<Int, Int>): AthleteEntity = AthleteEntity(
        athleteId = athleteId,
        userName = userName,
        receivedOn = receivedOn,
        profilePictureMedium = profilePictureMedium,
        profilePictureLarge = profilePictureLarge,
        firstName = firstName,
        lastName = lastName,
        yearMonthsCached = yearMonthsCached.plus(cache),
        gears = gears
    )

    fun withNewGearCaches(caches: List<Pair<String, String>>): AthleteEntity = AthleteEntity(
        athleteId = athleteId,
        userName = userName,
        receivedOn = receivedOn,
        profilePictureMedium = profilePictureMedium,
        profilePictureLarge = profilePictureLarge,
        firstName = firstName,
        lastName = lastName,
        yearMonthsCached = yearMonthsCached,
        gears = gears.plus(caches)
    )

}
