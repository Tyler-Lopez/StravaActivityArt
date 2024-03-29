package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.Athlete
import com.activityartapp.util.Response
import com.activityartapp.util.constants.CLIENT_SECRET
import com.activityartapp.util.constants.TokenConstants.CLIENT_ID
import java.util.concurrent.CancellationException
import javax.inject.Inject

/** Retrieves an [Athlete] by sending an authorization code to the Strava API. **/
class GetAccessTokenWithAuthorizationCodeFromRemote @Inject constructor(private val api: AthleteApi) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    suspend operator fun invoke(
        authorizationCode: String
    ): Response<Athlete> {
        return try {
            val bearer = api.getAccessToken(
                clientId = CLIENT_ID,
                clientSecret = CLIENT_SECRET,
                code = authorizationCode,
                grantType = GRANT_TYPE
            )
            Response.Success(object : Athlete {
                override val athleteId: Long = bearer.athlete.id
                override val lastCachedUnixMs: Long? = null
                override val expiresAtUnixSeconds = bearer.expiresAtUnixSeconds
                override val accessToken: String = bearer.accessToken
                override val refreshToken: String = bearer.refreshToken
            })
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Response.Error(exception = e)
        }
    }
}