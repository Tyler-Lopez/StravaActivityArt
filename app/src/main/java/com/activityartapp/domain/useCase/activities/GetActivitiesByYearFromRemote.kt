package com.activityartapp.domain.useCase.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.useCase.athleteUsage.GetAthleteUsageFromRemote
import com.activityartapp.domain.useCase.athleteUsage.InsertAthleteUsageIntoRemote
import com.activityartapp.util.Response
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.errors.AthleteRateLimitedException
import javax.inject.Inject

class GetActivitiesByYearFromRemote @Inject constructor(
    private val getActivitiesByPageFromRemote: GetActivitiesByPageFromRemote,
    private val getAthleteUsageFromRemote: GetAthleteUsageFromRemote,
    private val insertAthleteUsageIntoRemote: InsertAthleteUsageIntoRemote,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 200
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val FIRST_PAGE = 1
        private const val MAXIMUM_USAGE = 25
    }

    /**
     * @param startMonth Optional parameter to specify the first 0-indexed month which to read
     * from remote. Activities in months which precede this parameter will be omitted.
     */
    suspend operator fun invoke(
        accessToken: String,
        athleteId: Long,
        year: Int,
        startMonth: Int = FIRST_MONTH_OF_YEAR
    ): Response<List<Activity>> {

        var page = FIRST_PAGE
        var activitiesInLastPage = ACTIVITIES_PER_PAGE
        val activities = mutableListOf<Activity>()

        var usage: Int
        getAthleteUsageFromRemote(athleteId)
            .run {
                when (this) {
                    is Response.Success -> usage = data
                    is Response.Error -> return Response.Error(
                        activities,
                        exception
                    )
                }
            }
        println("GetActivitiesByYearFromRemote: usage is $usage")

        while (activitiesInLastPage >= ACTIVITIES_PER_PAGE) {
            if (usage >= MAXIMUM_USAGE) {
                println("$usage is more than max usage of $MAXIMUM_USAGE")
                return Response.Error(exception = AthleteRateLimitedException)
            }
            getActivitiesByPageFromRemote(
                code = accessToken,
                page = page++,
                activitiesPerPage = ACTIVITIES_PER_PAGE,
                beforeUnixSeconds = timeUtils.firstUnixSecondAfterYearMonth(
                    year, LAST_MONTH_OF_YEAR
                ),
                afterUnixSeconds = timeUtils.lastUnixSecondBeforeYearMonth(
                    year, startMonth
                ),
            )
                .doOnSuccess {
                    insertAthleteUsageIntoRemote(athleteId, ++usage)
                    activitiesInLastPage = data.size
                    activities.addAll(data)
                }
                .doOnError {
                    println("Here, an error occurred, that error was ${this.exception}")
                    return Response.Error(data = activities, exception = this.exception)
                }
        }

        return Response.Success(activities)
    }
}