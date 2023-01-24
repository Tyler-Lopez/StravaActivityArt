package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Activity
import javax.inject.Inject

class GetActivitiesByYearFromDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long, year: Int): List<Activity>? {
        return athleteDatabase
            .activityDao
            .getActivitiesByYear(athleteId, year)
    }
}