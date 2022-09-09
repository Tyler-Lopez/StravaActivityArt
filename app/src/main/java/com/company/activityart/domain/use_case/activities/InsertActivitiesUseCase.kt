package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.company.activityart.domain.use_case.athlete.InsertAthleteUseCase
import javax.inject.Inject

class InsertActivitiesUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase,
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase
) {
    suspend operator fun invoke(
        activities: List<Activity>,
        athleteId: Long,
        year: Int
    ) {
       // val  = getAthleteFromLocalUseCase(athleteId)?.cachedYearMonths


       // prevCache += year to activities

        val activityEntities = activities
            .map { it as ActivityEntity }
            .toTypedArray()

        athleteDatabase
            .activityDao
            .insertAllActivities(*activityEntities)
    }
}