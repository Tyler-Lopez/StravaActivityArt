package com.activityartapp.domain.useCase.athleteUsage

import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Inserts an update [Athlete]'s usage of the Strava API, reset twice daily,
 *  into remote. **/
class InsertAthleteUsageIntoRemote @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long, usage: Int) {
        athleteUsageRepository.insertAthleteUsage(athleteId.toString(), usage)
    }
}