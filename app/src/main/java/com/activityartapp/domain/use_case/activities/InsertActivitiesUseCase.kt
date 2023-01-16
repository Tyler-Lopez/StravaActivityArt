package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.ActivityEntity
import com.activityartapp.data.entities.AthleteEntity
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.activityartapp.domain.use_case.athlete.InsertAthleteUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InsertActivitiesUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase,
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase
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
            .map { it.run {
                ActivityEntity(
                    athleteId, averageSpeed, distance, gearId, id, kudosCount, locationCity, locationCountry, locationState, maxSpeed, movingTime, name, sufferScore, iso8601LocalDate, summaryPolyline, type
                )
            } }
            .toTypedArray()

        athleteDatabase
            .activityDao
            .insertAllActivities(*activityEntities)

        /** Update athlete cache for inserted activities **/
        println("Trying to get previous athlete")
        val prevAthlete = getAthleteFromLocalUseCase(athleteId)
        println("Prev athlete was $prevAthlete")
        (prevAthlete ?: object : Athlete {
            override val athleteId: Long = athleteId
            override val lastCachedYearMonth: Map<Int, Int> = mapOf()
        }).lastCachedYearMonth.let { prevCache ->
            val newCache = prevCache.toMutableMap()
            newCache[year] = lastStableMonth
            apply {
                insertAthleteUseCase(
                    AthleteEntity(
                        athleteId,
                        // TODO COMMENTED FOR NOW   userName,
                        // TODO COMMENTED FOR NOW   receivedOnUnixSeconds ?: TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt(),
                        // TODO COMMENTED FOR NOW     profilePictureMedium,
                        // TODO COMMENTED FOR NOW      profilePictureLarge,
                        // TODO COMMENTED FOR NOW       firstName,
                        // TODO COMMENTED FOR NOW       lastName,
                        newCache
                    )
                )
            }
        }

    }
}