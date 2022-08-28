package com.company.activityart.domain.use_case.athlete

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Athlete
import javax.inject.Inject

class GetAthleteFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): Athlete? {
        return athleteDatabase
            .athleteDao
            .getAthleteById(athleteId)
    }
}