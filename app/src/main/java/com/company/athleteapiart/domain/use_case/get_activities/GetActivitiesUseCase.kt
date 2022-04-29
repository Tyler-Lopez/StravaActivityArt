package com.company.athleteapiart.domain.use_case.get_activities

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Activities
import com.company.athleteapiart.data.remote.responses.Bearer
import com.company.athleteapiart.util.OAuth2
import com.company.athleteapiart.util.Resource
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getActivities(
        page: Int,
        perPage: Int,
        before: Int,
        after: Int
    ): Resource<Activities> {
        val response = try {
            api.getActivities(
                authHeader = "Bearer " + OAuth2.accessToken,
                page = page,
                perPage = perPage,
                before = before,
                after = after
            )
        } catch (e: Exception) {
            return Resource.Error("${e.message}")
        }
        return Resource.Success(response)
    }
}