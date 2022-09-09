package com.company.activityart.data.entities

import androidx.room.Entity
import androidx.room.TypeConverters
import com.company.activityart.data.Converters
import com.company.activityart.domain.models.Athlete

@Entity(primaryKeys = ["athleteId"])
data class AthleteEntity(
    override val athleteId: Long,
    override val userName: String?,
    // This is important as Strava does not want you caching data for more than 3 days
    override val receivedOnUnixSeconds: Int,
    override val profilePictureMedium: String,
    override val profilePictureLarge: String,
    override val firstName: String,
    override val lastName: String,

    @TypeConverters(Converters::class)
    override val lastCachedYearMonth: Map<Int, Int>
    // This is to tell us what we have and have not yet mapped to Room
    /*
    override val yearMonthsCached: Map<Int, Int>,
    override val gears: Map<String, String>

     */
) : Athlete {

    /*
    fun lastCachedMonth(year: Int): Int = yearMonthsCached.getOrDefault(year, -1)

    fun withNewCaches(cache: Map<Int, Int>): AthleteEntity = AthleteEntity(
        athleteId = athleteId,
        userName = userName,
        receivedOnUnixSeconds = receivedOnUnixSeconds,
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
        receivedOnUnixSeconds = receivedOnUnixSeconds,
        profilePictureMedium = profilePictureMedium,
        profilePictureLarge = profilePictureLarge,
        firstName = firstName,
        lastName = lastName,
        yearMonthsCached = yearMonthsCached,
        gears = gears.plus(caches)
    )


     */
}
