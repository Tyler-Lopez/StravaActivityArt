package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.Month
import java.time.Year
import java.util.*
import javax.inject.Inject

/**
 * Retrieves all [Activity] associated with the authenticated [Athlete]
 * which occurred on a year from local and remote repositories.
 */
class GetActivitiesByYearUseCase @Inject constructor(
    private val getActivitiesByYearMonthFromLocalUseCase: GetActivitiesByYearMonthFromLocalUseCase,
    private val getActivitiesByYearFromRemoteUseCase: GetActivitiesByYearFromRemoteUseCase,
    private val getActivitiesByYearMonthFromRemoteUseCase: GetActivitiesByYearMonthFromRemoteUseCase,
) {
    companion object {
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val MONTHS_IN_YEAR = 12
        private const val NO_CACHED_MONTHS = -1
    }

    /**
     * @param lastCachedMonth
     */
    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        year: Int,
        lastCachedMonth: Int = NO_CACHED_MONTHS
    ): Resource<List<Activity>> {

        val toReturn = mutableListOf<Activity>()

        /** Add all months less than [lastCachedMonth] to returning List **/
        loadLocalCachedActivitiesByYear(athleteId, year, lastCachedMonth)
            .forEach { monthlyActivities ->
                toReturn += monthlyActivities.second
            }

        /** If no months were cached, return remote response **/

        //   if (lastCachedMonth == NO_CACHED_MONTHS) {
        //      return getActivitiesByYearFromRemoteUseCase(accessToken, year)
        //  } else {
        val lastAdjMonth = when (Year.now().value) {
            year -> Calendar.getInstance().get(Calendar.MONTH)
            else -> LAST_MONTH_OF_YEAR
        }
        println("here month is " + Calendar.getInstance().get(Calendar.MONTH))
        val remainingMonthActivities: MutableList<Deferred<List<Activity>>> = mutableListOf()
        coroutineScope {
            ((lastCachedMonth + 1)..lastAdjMonth).forEach {
                remainingMonthActivities.add(async {
                    (getActivitiesByYearMonthFromRemoteUseCase(
                        accessToken, year, month = it
                    ) as Success).data
                })
            }
            //     }
            toReturn += remainingMonthActivities.awaitAll().flatten()
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