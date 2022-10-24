package com.company.activityart.domain.use_case.activities

import com.company.activityart.domain.models.Activity
import com.company.activityart.util.Response
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.doOnError
import com.company.activityart.util.doOnSuccess
import javax.inject.Inject

class GetActivitiesByYearMonthFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 50
        private const val FIRST_PAGE = 1
    }

    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Response<List<Activity>> {

        var page = FIRST_PAGE
        var activitiesInPage = ACTIVITIES_PER_PAGE
        val activities = mutableListOf<Activity>()

        while (activitiesInPage >= ACTIVITIES_PER_PAGE) {
            getActivitiesInYearByPageFromRemoteUseCase(
                code = accessToken,
                page = page++,
                activitiesPerPage = ACTIVITIES_PER_PAGE,
                beforeUnixSeconds = timeUtils.firstUnixSecondAfterYearMonth(
                    year, month
                ),
                afterUnixSeconds = timeUtils.lastUnixSecondBeforeYearMonth(
                    year, month
                ),
            )
                .doOnSuccess {
                    activitiesInPage = data.size
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