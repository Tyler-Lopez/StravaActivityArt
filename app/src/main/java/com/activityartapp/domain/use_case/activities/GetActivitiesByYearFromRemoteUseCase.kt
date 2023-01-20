package com.activityartapp.domain.use_case.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.use_case.athleteUsage.GetAthleteUsage
import com.activityartapp.domain.use_case.athleteUsage.InsertAthleteUsage
import com.activityartapp.util.Response
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.errors.AthleteRateLimited
import javax.inject.Inject

class GetActivitiesByYearFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
    private val getAthleteUsage: GetAthleteUsage,
    private val insertAthleteUsage: InsertAthleteUsage,
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
        initialAthleteUsage: Int,
        onAthleteUsageChanged: (Int) -> Unit,
        year: Int,
        startMonth: Int = FIRST_MONTH_OF_YEAR
    ): Response<List<Activity>> {

        var usage = initialAthleteUsage
        println("GetActivitiesByYearFromRemoteUseCase: usage is $usage")

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
                    insertAthleteUsage(athleteId, ++usage)
                    onAthleteUsageChanged(usage)
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