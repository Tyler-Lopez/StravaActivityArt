package com.activityartapp.domain.useCase.athleteUsage

import com.activityartapp.domain.AthleteUsageRepository
import com.activityartapp.util.Response
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Retrieves an [Athlete]'s usage of the Strava API, reset twice daily, from remote. **/
class GetAthleteUsageFromRemote @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long): Response<Int> {
        return athleteUsageRepository.getAthleteUsage(athleteId.toString())
    }
}