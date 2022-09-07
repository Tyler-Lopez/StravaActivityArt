package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.Activity
import javax.inject.Inject

class GetActivitiesByYearMonthFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    /*
    suspend operator fun invoke(athleteId: Long, year: Int, month: Int): List<Activity> {
        return athleteDatabase
            .activityDao
            .getActivitiesByYearMonth(athleteId, year, month)
    }

     */
}