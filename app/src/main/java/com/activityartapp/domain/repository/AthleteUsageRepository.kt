package com.activityartapp.domain.repository

import com.activityartapp.util.Response
import com.google.common.annotations.VisibleForTesting

interface AthleteUsageRepository {

    companion object {
        const val NO_USAGE_FOUND = 0
    }

    suspend fun getAthleteUsage(athleteId: String): Response<Int>
    suspend fun insertAthleteUsage(athleteId: String, usage: Int)
}