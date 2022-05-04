package com.company.athleteapiart.domain.use_case.get_access_token

import android.content.Context
import com.company.athleteapiart.data.database.OAuth2Database
import com.company.athleteapiart.data.entities.OAuth2Entity
import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Bearer
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    private val clientId = 75992

    // Invoked publicly, checks Room database for previous entry
    // Returns error if not yet connected
    suspend fun getAccessToken(context: Context): Resource<Bearer> {

        val oAuth2Entity = OAuth2Database
            .getInstance(context.applicationContext)
            .oAuth2Dao
            .getOauth2()

        println("Here, read oAuth as $oAuth2Entity and ${oAuth2Entity?.accessToken} and ${oAuth2Entity?.firstName}")
        println(oAuth2Entity)

        return when {
            else -> Resource.Error("User has not yet authenticated with Strava")
        }
    }

    suspend fun getAccessTokenFromAuthorizationCode(
        code: String
    ): Resource<Bearer> {
        val response = try {
            api.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                code = code,
                grantType = "authorization_code"
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred retrieving access token. ${e.message}")
        }

        return Resource.Success(response)
    }

    // Invoked privately only if the Room database access token is expired
    private suspend fun getAccessTokenFromRefreshToken(
        clientId: Int,
        clientSecret: String,
        code: String,
    ): Resource<Bearer> {

        val response = try {
            api.getAccessTokenFromRefresh(
                clientId = clientId,
                clientSecret = clientSecret,
                refreshToken = code
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred retrieving access token from refresh. ${e.message}")
        }

        return Resource.Success(response)
    }
}