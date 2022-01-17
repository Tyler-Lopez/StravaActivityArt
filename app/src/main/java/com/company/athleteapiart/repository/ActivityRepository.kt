package com.company.athleteapiart.repository

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Activities
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import com.company.athleteapiart.data.remote.responses.Bearer
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2
import com.company.athleteapiart.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

// https://youtu.be/aaChg9aJDW4?t=699

@ActivityScoped
class ActivityRepository @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getAccessToken(
        clientId: Int,
        clientSecret: String,
        code: String,
        grantType: String
    ): Resource<Bearer> {
        val response = try {
            api.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                code = code,
                grantType = grantType
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }

        return Resource.Success(response)
    }

    suspend fun getActivities(
        page: Int,
        perPage: Int,
        before: Int,
        after: Int
    ): Resource<Activities> {
        val response = try {
            api.getActivities(
                authHeader = "Bearer " + Oauth2.accessToken,
                page = page,
                perPage = perPage,
                before = before,
                after = after
            )
        } catch (e: Exception) {
            return Resource.Error("${e.message} An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    /*
    suspend fun getActivity(

    ): Resource<ActivityDetailed> {
        val response = try {
            api.getActivityDetailed(
                authHeader = "Bearer " + Oauth2.accessToken,
                id = AthleteActivities.selectedActivity.value?.id ?: 0L,
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.\n${e.message}")
        }
        return Resource.Success(response)
    }

     */
}