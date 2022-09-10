package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.use_case.athlete.GetLastCachedYearMonthsUseCase
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.Success
import com.company.activityart.util.TimeUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.Year
import java.util.*
import javax.inject.Inject

/**
 * Retrieves all [Activity] associated with the authenticated [Athlete]
 * which occurred on a year from local and remote repositories.
 */
class GetActivitiesByYearUseCase @Inject constructor(
    private val getAthleteCachedYearMonthsUseCase: GetLastCachedYearMonthsUseCase,
    private val getActivitiesByYearMonthFromLocalUseCase: GetActivitiesByYearMonthFromLocalUseCase,
    private val getActivitiesByYearFromRemoteUseCase: GetActivitiesByYearFromRemoteUseCase,
    private val insertActivitiesUseCase: InsertActivitiesUseCase,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val NO_CACHED_MONTHS = -1
    }

    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        year: Int,

    ): Resource<List<Activity>> {

        val toReturn = mutableListOf<Activity>()

        val cachedYearMonths = getAthleteCachedYearMonthsUseCase(athleteId)
        val lastCachedMonth = cachedYearMonths[year] ?: NO_CACHED_MONTHS

        /** Add all months less than lastCachedMonth to returning List **/
        loadLocalCachedActivitiesByYear(athleteId, year, lastCachedMonth)
            .forEach { monthlyActivities ->
                toReturn += monthlyActivities.second
            }

        if (lastCachedMonth != LAST_MONTH_OF_YEAR) {
            getActivitiesByYearFromRemoteUseCase(
                accessToken = accessToken,
                year = year,
                startMonth = cachedYearMonths[year].takeIf {
                    it != NO_CACHED_MONTHS
                }?.plus(1) ?: FIRST_MONTH_OF_YEAR
            ).doOnSuccess {

                val cal = Calendar.getInstance()
                val currMonth = cal.get(Calendar.MONTH)
                val currYear = cal.get(Calendar.YEAR)
                val lastStableMonth = if (currYear == year) {
                    currMonth - 1
                } else {
                    LAST_MONTH_OF_YEAR
                }
                toReturn += data

                /** Cache received activities from remote, excl currMonth **/
                data.filter {
                    if (currYear == year) {
                        timeUtils.iso8601StringToMonth(it.iso8601LocalDate) != currMonth
                    } else {
                        true
                    }
                }
                    .let {
                        lastStableMonth.takeIf { it >= 0 }?.let { month ->
                            insertActivitiesUseCase(it, athleteId, year, month)
                        }
                    }

            }
        }

        return Success(toReturn)
    }

    private suspend fun loadLocalCachedActivitiesByYear(
        athleteId: Long,
        year: Int,
        lastCachedMonth: Int
    ): List<Pair<Int, List<Activity>>> {
        val localYearActivitiesDeferred =
            mutableListOf<Deferred<Pair<Int, List<Activity>>>>()
        coroutineScope {
            (FIRST_MONTH_OF_YEAR..lastCachedMonth).forEach {
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