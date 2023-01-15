package com.activityartapp.domain.use_case.athleteUsage

import com.activityartapp.domain.AthleteUsageRepository
import com.activityartapp.util.doOnSuccess
import javax.inject.Inject

class IncrementAthleteUsage @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long, prevUsage: Int) {
        println("Invoked increment athlete usage")
        athleteUsageRepository.insertAthleteUsage(athleteId.toString(), prevUsage + 1)
        println("Finished invoking increment athlete usage")
    }
}