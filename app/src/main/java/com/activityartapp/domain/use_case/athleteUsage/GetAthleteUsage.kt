package com.activityartapp.domain.use_case.athleteUsage

import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.AthleteUsageRepository
import com.activityartapp.domain.models.dataExpired
import com.activityartapp.util.Response
import javax.inject.Inject

class GetAthleteUsage @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long): Response<Int> {
        return athleteUsageRepository.getAthleteUsage(athleteId.toString())
    }
}