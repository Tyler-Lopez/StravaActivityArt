package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Activity
import com.company.activityart.util.Resource
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetActivitiesInYearFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByYearByPageFromRemoteUseCase,
    private val api: AthleteApi // Impl of API
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 200
    }

}