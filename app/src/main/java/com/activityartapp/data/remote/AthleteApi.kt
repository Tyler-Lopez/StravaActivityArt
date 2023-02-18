package com.activityartapp.data.remote

import com.activityartapp.data.remote.responses.*
import retrofit2.http.*

interface AthleteApi {
    @POST("oauth/token?")
    suspend fun getAccessToken(
        @Query("client_id") clientId: Int,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String
    ): Bearer

    @POST("oauth/token?")
    suspend fun getAccessTokenFromRefresh(
        @Query("client_id") clientId: Int,
        @Query("client_secret") clientSecret: String,
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType: String = "refresh_token"
    ): BearerWithoutAthlete

    // Get activities from access token
    @GET("api/v3/athlete/activities?")
    suspend fun getActivities(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Activities
}