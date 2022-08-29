package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.util.Resource
import com.company.activityart.util.TokenConstants.CLIENT_ID
import com.company.activityart.util.TokenConstants.CLIENT_SECRET
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetAccessTokenFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi
) {
    companion object {
        private const val GRANT_TYPE = "refresh_token"
    }

    suspend operator fun invoke(
        authorizationCode: String
    ): Resource<OAuth2> {
        return try {
            Resource.Success(
                api.getAccessTokenFromRefresh(
                    clientId = CLIENT_ID,
                    clientSecret = CLIENT_SECRET,
                    refreshToken = authorizationCode,
                    grantType = GRANT_TYPE
                )
            )
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Resource.Error(exception = e)
        }
    }
}