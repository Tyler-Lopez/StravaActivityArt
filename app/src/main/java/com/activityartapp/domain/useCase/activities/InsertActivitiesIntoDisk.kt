package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.ActivityEntity
import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class InsertActivitiesIntoDisk @Inject constructor(private val athleteDatabase: AthleteDatabase) {
    suspend operator fun invoke(
        activities: List<Activity>,
        athleteId: Long
    ) {
        val activityEntities = activities
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
                        sportType
                    )
                }
            }
            .toTypedArray()

        athleteDatabase
            .activityDao
            .insertAllActivities(*activityEntities)
    }
}