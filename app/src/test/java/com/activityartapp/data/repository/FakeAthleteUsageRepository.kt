package com.activityartapp.data.repository

import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.util.Response
import com.google.common.annotations.VisibleForTesting

@VisibleForTesting
internal class FakeAthleteUsageRepository : AthleteUsageRepository {

    private val athleteUsage = mutableMapOf<String, Int>()

    override suspend fun getAthleteUsage(athleteId: String): Response<Int> {
        return Response.Success(athleteUsage[athleteId] ?: AthleteUsageRepository.NO_USAGE_FOUND)
    }

    override suspend fun insertAthleteUsage(athleteId: String, usage: Int) {
        athleteUsage[athleteId] = usage
    }
}