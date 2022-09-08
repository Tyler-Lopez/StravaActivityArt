package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.util.Resource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetActivitiesByYearUseCase @Inject constructor(
    private val getActivitiesByYearMonthFromLocalUseCase: GetActivitiesByYearMonthFromLocalUseCase,
    private val getActivitiesByYearFromRemoteUseCase: GetActivitiesByYearFromRemoteUseCase,
) {
    companion object {
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
    }

    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        year: Int
    ): Resource<List<Activity>> {

        val toReturn = mutableListOf<Activity>()

        val localActivities = loadLocalActivitiesInYear(athleteId, year)
        val missingMonths = mutableListOf<Int>()

        localActivities.forEach { monthlyActivities ->
            monthlyActivities.second?.let {
                toReturn += it
            } ?: run { missingMonths += monthlyActivities.first }
        }

        
    }

    private suspend fun loadLocalActivitiesInYear(
        athleteId: Long,
        year: Int
    ): List<Pair<Int, List<Activity>?>> {
        val localYearActivitiesDeferred =
            mutableListOf<Deferred<Pair<Int, List<Activity>?>>>()
        coroutineScope {
            (FIRST_MONTH_OF_YEAR..LAST_MONTH_OF_YEAR).forEach {
                localYearActivitiesDeferred.add(async {
                    Pair(
                        first = it,
                        second = getActivitiesByYearMonthFromLocalUseCase(
                            athleteId = athleteId,
                            month = it,
                            year = year
                        )
                    )
                })
            }
        }
        return localYearActivitiesDeferred.awaitAll()
    }
}