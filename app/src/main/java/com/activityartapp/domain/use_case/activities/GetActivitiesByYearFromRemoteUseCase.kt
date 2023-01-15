package com.activityartapp.domain.use_case.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.use_case.athleteUsage.GetAthleteUsage
import com.activityartapp.domain.use_case.athleteUsage.IncrementAthleteUsage
import com.activityartapp.util.Response
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.errors.AthleteRateLimited
import javax.inject.Inject

class GetActivitiesByYearFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
    private val getAthleteUsage: GetAthleteUsage,
    private val incrementAthleteUsage: IncrementAthleteUsage,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 200
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val FIRST_PAGE = 1
        private const val INITIAL_USAGE = 0
        private const val MAXIMUM_USAGE = 10
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

        println("Getting usage")
        var usage = getAthleteUsage(athleteId).data ?: INITIAL_USAGE
        println("After usage, usage is $usage")

        var page = FIRST_PAGE
        var activitiesInLastPage = ACTIVITIES_PER_PAGE
        val activities = mutableListOf<Activity>()

        while (activitiesInLastPage >= ACTIVITIES_PER_PAGE) {
            if (usage >= MAXIMUM_USAGE) {
                println("$usage is more than max usage of $MAXIMUM_USAGE")
                return Response.Error(exception = AthleteRateLimited)
            }
            getActivitiesInYearByPageFromRemoteUseCase(
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
                    incrementAthleteUsage(athleteId, usage++)
                    activitiesInLastPage = data.size
                    activities.addAll(data)
                }
                .doOnError {
                    /**
                     * Todo, handle error better here
                     * Ex partial return?
                     */
                    return this
                }
        }

        return Response.Success(activities)
    }
}