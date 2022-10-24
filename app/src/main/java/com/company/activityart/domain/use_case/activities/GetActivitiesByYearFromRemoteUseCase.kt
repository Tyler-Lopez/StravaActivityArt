package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.util.Response
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.doOnError
import com.company.activityart.util.doOnSuccess
import javax.inject.Inject

class GetActivitiesByYearFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 200
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val FIRST_PAGE = 1
    }

    /**
     * @param startMonth Optional parameter to specify the first 0-indexed month which to read
     * from remote. Activities in months which precede this parameter will be omitted.
     */
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        startMonth: Int = FIRST_MONTH_OF_YEAR
    ): Response<List<Activity>> {

        var page = FIRST_PAGE
        var activitiesInLastPage = ACTIVITIES_PER_PAGE
        val activities = mutableListOf<Activity>()

        while (activitiesInLastPage >= ACTIVITIES_PER_PAGE) {
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