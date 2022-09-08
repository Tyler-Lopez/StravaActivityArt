package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
    }

    /**
     * @param cachedMonths The months known to be cached of this year associated
     * with an [Athlete]. Providing this distinguishes between months which
     * are known to have no activities from those which have never been fetched
     * to prevent unnecessary API requests.
     */
    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        year: Int,
        cachedMonths: List<Int>
    ): Resource<List<Activity>> {

        val toReturn = mutableListOf<Activity>()

        /** Add all [cachedMonths] to returning List **/
        loadLocalCachedActivitiesByYear(athleteId, year, cachedMonths)
            .forEach { monthlyActivities ->
                toReturn += monthlyActivities.second
            }

        /** Filter any [cachedMonths] from those we must fetch remotely **/
        (FIRST_MONTH_OF_YEAR..LAST_MONTH_OF_YEAR)
            .subtract(cachedMonths.toSet())
            .apply {
                /** If no months were cached, fetch all activities **/
                if (size == MONTHS_IN_YEAR) {
                    getActivitiesByYearFromRemoteUseCase(
                        accessToken, year
                    )
                        .doOnSuccess {
                            toReturn.addAll(data)
                        }
                        .doOnError {
                            // Todo
                        }
                } else {
                    forEach { month ->
                        getActivitiesByYearMonthFromRemoteUseCase(
                            accessToken, year, month
                        )
                            .doOnSuccess {
                                toReturn.addAll(data)
                            }
                            .doOnError {
                                // Todo
                            }
                    }
                }
            }

        return Success(toReturn)
    }

    private suspend fun loadLocalCachedActivitiesByYear(
        athleteId: Long,
        year: Int,
        cachedMonths: List<Int>
    ): List<Pair<Int, List<Activity>>> {
        val localYearActivitiesDeferred =
            mutableListOf<Deferred<Pair<Int, List<Activity>>>>()
        coroutineScope {
            cachedMonths.forEach {
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