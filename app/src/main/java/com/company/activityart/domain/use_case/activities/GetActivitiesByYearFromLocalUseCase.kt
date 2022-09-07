package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.dataExpired
import javax.inject.Inject

class GetActivitiesByYearFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    /*
    suspend operator fun invoke(athleteId: Long, year: Int): List<Activity> {
        return athleteDatabase
            .activityDao
            .getActivitiesByYear(athleteId, year)
    }

     */
}