package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class GetActivitiesByYearUpToMonthFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(
        athleteId: Long,
        year: Int,
        upToMonth: Int
    ): List<Activity> {
        return athleteDatabase
            .activityDao
            .getActivitiesByYearUpToMonth(athleteId, upToMonth, year)
    }
}