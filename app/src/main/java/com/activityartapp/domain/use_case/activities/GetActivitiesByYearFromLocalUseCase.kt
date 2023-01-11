package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class GetActivitiesByYearFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long, year: Int): List<Activity>? {
        return athleteDatabase
            .activityDao
            .getActivitiesByYear(athleteId, year)
    }
}