package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.ActivityEntity
import com.activityartapp.data.entities.AthleteCacheDictionaryEntity
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.AthleteCacheDictionary
import com.activityartapp.domain.useCase.athleteCacheDictionary.GetAthleteCacheDictionaryFromDisk
import com.activityartapp.domain.useCase.athleteCacheDictionary.InsertAthleteCacheDictionaryIntoDisk
import javax.inject.Inject

class InsertActivitiesIntoDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase,
    private val getAthleteCacheDictionaryFromDisk: GetAthleteCacheDictionaryFromDisk,
    private val insertAthleteCacheDictionaryIntoDisk: InsertAthleteCacheDictionaryIntoDisk
) {
    suspend operator fun invoke(
        activities: List<Activity>,
        athleteId: Long,
        year: Int,
        lastStableMonth: Int
    ) {
        println("Insert activities use case invoked for year $year, last stable month $lastStableMonth")

        val activityEntities = activities
            // Todo, replace with general to ActivityEntity function
            .map {
                it.run {
                    ActivityEntity(
                        athleteId,
                        averageSpeed,
                        distance,
                        gearId,
                        id,
                        kudosCount,
                        locationCity,
                        locationCountry,
                        locationState,
                        maxSpeed,
                        movingTime,
                        name,
                        sufferScore,
                        iso8601LocalDate,
                        summaryPolyline,
                        type
                    )
                }
            }
            .toTypedArray()

        athleteDatabase
            .activityDao
            .insertAllActivities(*activityEntities)

        /** Update athlete cache for inserted activities **/
        println("Trying to get previous athlete")
        val athleteCacheDictionary = getAthleteCacheDictionaryFromDisk(athleteId) ?: object : AthleteCacheDictionary {
            override val id: Long = athleteId
            override val lastCachedYearMonth: Map<Int, Int> = mapOf()
        }
        println("Prev athlete was $athleteCacheDictionary")
        athleteCacheDictionary.run {
            lastCachedYearMonth.let { prevCache ->
                val newCache = prevCache.toMutableMap()
                newCache[year] = lastStableMonth
                apply {
                    insertAthleteCacheDictionaryIntoDisk(
                        AthleteCacheDictionaryEntity(
                            id = athleteId,
                            lastCachedYearMonth = newCache
                        )
                    )
                }
            }
        }
    }
}