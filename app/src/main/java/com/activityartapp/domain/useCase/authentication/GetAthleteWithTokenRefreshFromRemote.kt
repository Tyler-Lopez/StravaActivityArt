package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.models.requiresRefresh
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Error
import com.activityartapp.util.Response.Success
import com.activityartapp.util.constants.CLIENT_SECRET
import com.activityartapp.util.constants.TokenConstants
import java.util.concurrent.CancellationException
import javax.inject.Inject

/** Retrieves a non-expired [Athlete] from the potentially-expired access token
 * and refresh token stored on disk. We refresh an expired access token by the
 * Strava API. Returns an [Error] if no token exists on disk or when unable
 * to refresh an expired token.**/
class GetAthleteWithTokenRefreshFromRemote @Inject constructor(
    private val athleteApi: AthleteApi,
    private val getAccessTokenFromLocalUseCase: GetAthleteFromDisk,
) {
    companion object {
        private const val GRANT_TYPE = "refresh_token"
    }

    suspend operator fun invoke(): Response<Athlete> {
        getAccessTokenFromLocalUseCase().apply {
            return when {
                this == null -> Error()
                /** On refresh, pass in local athlete id as refresh does not incl AthleteHasCached **/
                requiresRefresh -> onRequiresRefresh(this)
                else -> Success(this)
            }
        }
    }

    private suspend fun onRequiresRefresh(prevOauth: Athlete):
            Response<Athlete> {
        return try {
            println("refresh required, refreshing...")
            Success(
                athleteApi.getAccessTokenFromRefresh(
                    clientId = TokenConstants.CLIENT_ID,
                    clientSecret = CLIENT_SECRET,
                    refreshToken = prevOauth.refreshToken,
                    grantType = GRANT_TYPE
                ).run {
                    val expiresAtUnixSeconds = expiresAtUnixSeconds
                    val accessToken = accessToken
                    val refreshToken = refreshToken
                    object : Athlete {
                        override val athleteId: Long = prevOauth.athleteId
                        override val lastCachedUnixMs: Long? = null
                        override val expiresAtUnixSeconds: Int = expiresAtUnixSeconds
                        override val accessToken: String = accessToken
                        override val refreshToken: String = refreshToken
                    }
                }
            )
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Error(data = prevOauth, exception = e)
        }
    }
}