package com.activityartapp.domain.use_case.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.domain.use_case.athlete.GetLastCachedYearMonthsUseCase
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Success
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
    private val getActivitiesFromCacheUseCase: GetActivitiesByYearFromCacheUseCase,
    private val insertActivitiesIntoCacheUseCase: InsertActivitiesIntoCacheUseCase,
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
    ): Response<List<Activity>> {

        /** If Singleton RAM cache has been populated for this year prev, return that **/
        getActivitiesFromCacheUseCase(year)?.apply { return Success(this) }

        val toReturn = mutableListOf<Activity>()

        /** Read from ROOM storage cache **/
        val cachedYearMonths = getAthleteCachedYearMonthsUseCase(athleteId)
        val lastCachedMonth = cachedYearMonths[year] ?: NO_CACHED_MONTHS

        loadLocalCachedActivitiesByYear(athleteId, year, lastCachedMonth)
            .forEach { monthlyActivities ->
                toReturn += monthlyActivities.second
            }

        /** If any months of this year were not in ROOM storage cache,
         * receive from remote */
        if (lastCachedMonth != LAST_MONTH_OF_YEAR) {
            println("Last cached month: $lastCachedMonth")
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
                .doOnError {
                    println("Here, error $exception")
                    return Response.Error(
                        data = toReturn,
                        exception = exception
                    )
                }
        }

        /** Return successful result **/
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