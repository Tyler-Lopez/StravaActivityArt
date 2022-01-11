package com.company.athleteapiart.data.remote

import com.company.athleteapiart.data.remote.responses.Activities
import com.company.athleteapiart.data.remote.responses.Bearer
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AthleteApi {
    @POST("oauth/token?")
    suspend fun getAccessToken(
        @Query("client_id") clientId: Int,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String
    ): Bearer

    // Get activities from access token
    @GET("api/v3/athlete/activities?")
    suspend fun getActivities(
        @Query("access_token") accessToken: String,
        @Query("per_page") perPage: Int,
    ): Activities
}