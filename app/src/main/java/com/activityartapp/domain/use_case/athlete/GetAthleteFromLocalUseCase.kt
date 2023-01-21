package com.activityartapp.domain.use_case.athlete

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

class GetAthleteFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): Athlete? {
        return athleteDatabase
            .athleteDao
            .getAthleteById(athleteId)
            ?.takeIf {
                // todo evalute !it.dataExpired
                true
            }
    }
}