package com.activityartapp.domain.use_case.athlete

import com.activityartapp.data.database.AthleteDatabase
import javax.inject.Inject

class GetLastCachedYearMonthsUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): Map<Int, Int> {
        return athleteDatabase
            .athleteDao
            .getAthleteById(athleteId)
            ?.lastCachedYearMonth ?: mapOf()
    }
}