package com.activityartapp.domain

import com.activityartapp.util.Response

interface AthleteUsageRepository {
    suspend fun getAthleteUsage(athleteId: String): Response<Int>
    suspend fun insertAthleteUsage(athleteId: String, usage: Int)
}