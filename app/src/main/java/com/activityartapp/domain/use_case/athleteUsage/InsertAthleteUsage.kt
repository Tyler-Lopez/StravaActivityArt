package com.activityartapp.domain.use_case.athleteUsage

import com.activityartapp.domain.AthleteUsageRepository
import com.activityartapp.util.doOnSuccess
import javax.inject.Inject

class InsertAthleteUsage @Inject constructor(private val athleteUsageRepository: AthleteUsageRepository) {
    suspend operator fun invoke(athleteId: Long, usage: Int) {
        athleteUsageRepository.insertAthleteUsage(athleteId.toString(), usage)
    }
}