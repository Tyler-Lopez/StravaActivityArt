package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.company.activityart.domain.use_case.athlete.InsertAthleteUseCase
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
        val prevAthlete = getAthleteFromLocalUseCase(athleteId)
        prevAthlete?.lastCachedYearMonth.let { prevCache ->
            val newCache = prevCache?.toMutableMap() ?: mutableMapOf()
            newCache[year] = lastStableMonth
            prevAthlete?.apply {
                insertAthleteUseCase(
                    AthleteEntity(
                        athleteId,
                        userName,
                        receivedOnUnixSeconds ?: TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt(),
                        profilePictureMedium,
                        profilePictureLarge,
                        firstName,
                        lastName,
                        newCache
                    )
                )
            }
        }

    }
}