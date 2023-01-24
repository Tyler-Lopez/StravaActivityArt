package com.activityartapp.domain.useCase.athleteUsage

import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.util.Response
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Retrieves an [Athlete]'s usage of the Strava API, reset twice daily, from remote.
 * If an [Athlete]'s usage does not exist on remote, returns [Response.Success]
 * with the constant data value [AthleteUsageRepository.NO_USAGE_FOUND] **/
class GetAthleteUsageFromRemote @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long): Response<Int> {
        return athleteUsageRepository.getAthleteUsage(athleteId.toString())
    }
}