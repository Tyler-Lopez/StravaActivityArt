package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.util.Response
import com.company.activityart.util.constants.TokenConstants.CLIENT_ID
import com.company.activityart.util.constants.TokenConstants.CLIENT_SECRET
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetAccessTokenFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi
) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    suspend operator fun invoke(
        authorizationCode: String
    ): Response<OAuth2> {
        println("here auth code is $authorizationCode")
        return try {
            Response.Success(
                api.getAccessToken(
                    clientId = CLIENT_ID,
                    clientSecret = CLIENT_SECRET,
                    code = authorizationCode,
                    grantType = GRANT_TYPE
                )
            )
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Response.Error(exception = e)
        }
    }
}