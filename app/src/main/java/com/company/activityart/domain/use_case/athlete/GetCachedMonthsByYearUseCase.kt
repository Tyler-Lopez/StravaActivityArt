package com.company.activityart.domain.use_case.athlete

import com.company.activityart.data.database.AthleteDatabase
import javax.inject.Inject

class GetCachedMonthsByYearUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): Map<Int, List<Int>> {
        return athleteDatabase
            .athleteDao
            .getAthleteById(athleteId)
            ?.cachedYearMonths ?: mapOf()
    }
}