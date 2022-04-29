package com.company.athleteapiart.domain.use_case.get_access_token_refresh

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Bearer
import com.company.athleteapiart.util.Resource
import javax.inject.Inject

class GetAccessTokenRefreshUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getAccessToken(
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