package com.company.athleteapiart.domain.use_case.get_access_token

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Bearer
import com.company.athleteapiart.util.Resource
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
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
            return Resource.Error("An error occurred retrieving access token. ${e.message}")
        }

        return Resource.Success(response)
    }
}