package com.activityartapp.domain.useCase.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.models.AthleteCacheDictionary
import com.activityartapp.domain.useCase.athleteCacheDictionary.GetAthleteCacheDictionaryFromDisk
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Success
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Retrieves all [Activity] for an [Athlete] by their id and year.
 *
 * Activities will be sourced, in increasing preference, from the Strava API or disk storage.
 *
 * Activities are automatically stored on disk and in memory.
 */
class GetActivitiesByYearFromDiskOrRemote @Inject constructor(
    private val getAthleteCacheDictionaryFromDisk: GetAthleteCacheDictionaryFromDisk,
    private val getActivitiesByYearMonthFromDisk: GetActivitiesByYearMonthFromDisk,
    private val getActivitiesByYearFromRemote: GetActivitiesByYearFromRemote,
    private val insertActivitiesIntoDisk: InsertActivitiesIntoDisk,
    private val insertActivitiesIntoMemory: InsertActivitiesIntoMemory,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val NO_CACHED_MONTHS = -1
    }

    /**
     * @param accessToken A non-expired access token.
     * @param athleteId The athlete's ID as provided by Strava.
     * @param internetEnabled Whether or not we should include fetching from Strava API as
     * an option. For example, we may not want to allow a remote query if unable to validate
     * the version due to a connection issue.
     * @param year The year we are returning associated activities for.
     * **/
    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        internetEnabled: Boolean,
        year: Int,
    ): Response<List<Activity>> {
        return run {
            val activities = mutableListOf<Activity>()

            /** Determine from the [AthleteCacheDictionary] associated with this athlete
             * which months of this year have cached on disk storage. **/
            val cachedYearMonths =
                getAthleteCacheDictionaryFromDisk(athleteId)?.lastCachedYearMonth ?: mapOf()
            val lastCachedMonth = cachedYearMonths[year] ?: NO_CACHED_MONTHS

            activities += mutableListOf<Deferred<List<Activity>>>().apply {
                coroutineScope {
                    (FIRST_MONTH_OF_YEAR..lastCachedMonth).forEach {
                        add(async {
                            getActivitiesByYearMonthFromDisk(
                                athleteId = athleteId,
                                month = it,
                                year = year
                            )
                        })
                    }
                }
            }.awaitAll().flatten()

            /** If we've reached this point and internet access is disabled, send back [UnknownHostException] **/
            if (!internetEnabled) {
                return@run Response.Error(
                    data = activities.toList(),
                    exception = UnknownHostException()
                )
            }

            /** If any months of this [year] have not been stored on disk storage, obtain activities
             * of those months from a Strava query. */
            if (lastCachedMonth != LAST_MONTH_OF_YEAR) {
                getActivitiesByYearFromRemote(
                    accessToken = accessToken,
                    athleteId = athleteId,
                    year = year,
                    startMonth = cachedYearMonths[year].takeIf {
                        it != NO_CACHED_MONTHS
                    }?.nextMonth ?: FIRST_MONTH_OF_YEAR
                )
                    .doOnSuccess {
                        val cal = Calendar.getInstance()
                        val currMonth = cal.get(Calendar.MONTH)
                        val currYear = cal.get(Calendar.YEAR)

                        /** At this point, we have received from remote all [Activity] between the start
                         * month and the end of the year. However, we will never consider the current
                         * month of the current year cached. **/
                        val storeUpToMonthOnDisk = if (currYear == year) {
                            currMonth.previousMonth
                        } else {
                            LAST_MONTH_OF_YEAR
                        }

                        /** Prepare to return all activities, then filter out anything from the current month
                         * if this is the current year before caching on disk. **/
                        activities += data

                        /** Cache activities into disk **/
                        val filteredData = data
                            .takeIf { currYear == year }
                            ?.filter { timeUtils.iso8601StringToMonth(it.iso8601LocalDate) != currMonth }
                            ?: data

                        storeUpToMonthOnDisk.takeIf { it >= FIRST_MONTH_OF_YEAR }?.let { month ->
                            insertActivitiesIntoDisk(filteredData, athleteId, year, month)
                        }
                    }
                    .doOnError {
                        return@run Response.Error(
                            data = activities.toList(),
                            exception = exception
                        )
                    }
            }

            /** Return successful result **/
            return@run Success(data = activities.toList())
        }.also {
            /** Insert activities into memory **/
            it.data?.let { activities -> insertActivitiesIntoMemory(year, activities) }
        }
    }

    private val Int.previousMonth get() = this - 1
    private val Int.nextMonth get() = this + 1
}