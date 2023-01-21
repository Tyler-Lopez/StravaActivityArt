package com.activityartapp.domain.use_case.authentication

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.use_case.athlete.InsertAthleteUseCase
import com.activityartapp.util.Response
import com.activityartapp.util.constants.CLIENT_SECRET
import com.activityartapp.util.constants.TokenConstants.CLIENT_ID
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetAccessTokenFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi,
    private val insertAthleteUseCase: InsertAthleteUseCase
) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    suspend operator fun invoke(
        authorizationCode: String
    ): Response<OAuth2> {
        return try {
            val bearer = api.getAccessToken(
                clientId = CLIENT_ID,
                clientSecret = CLIENT_SECRET,
                code = authorizationCode,
                grantType = GRANT_TYPE
            )

            /** Slightly sloppy:
             * The reason we do this is that the first time we receive an
             * access-token (i.e. not refresh), the authenticated athlete
             * is given to us for "free".
             *
             * We insert that athlete immediately here which gives us access
             * to name, profile picture, and other properties. Though current
             * launch iteration of app does not utilize these.
             */
            insertAthleteUseCase(bearer.athlete)

            Response.Success(bearer)
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Response.Error(exception = e)
        }
    }
}