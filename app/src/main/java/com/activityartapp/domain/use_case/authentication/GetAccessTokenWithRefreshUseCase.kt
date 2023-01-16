package com.activityartapp.domain.use_case.authentication

import com.activityartapp.data.entities.OAuth2Entity
import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.data.remote.responses.Bearer
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.models.requiresRefresh
import com.activityartapp.util.Response
import com.activityartapp.util.Response.*
import com.activityartapp.util.constants.TokenConstants
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * Retrieves persisted authentication [OAuth2Entity] from local
 * storage and refreshes from remote expired.
 */
class GetAccessTokenWithRefreshUseCase @Inject constructor(
    private val athleteApi: AthleteApi,
    private val getAccessTokenFromLocalUseCase: GetAccessTokenFromLocalUseCase,
) {
    companion object {
        private const val GRANT_TYPE = "refresh_token"
    }

    suspend operator fun invoke(): Response<OAuth2> {
        getAccessTokenFromLocalUseCase().apply {
            println("getAccessTokenFromLocalUseCase invoked...")
            return when {
                this == null -> {
                    println("RESULT: NULL")
                    Error()
                }
                /** On refresh, pass in local athlete id as refresh does not incl Athlete **/
                requiresRefresh -> {
                    println("RESULT: REQUIRES REFRESH")
                    onRequiresRefresh(this)
                }
                else -> {
                    println("RESULT: SUCCESS")
                    Success(this)
                }
            }
        }
    }

    private suspend fun onRequiresRefresh(prevOauth: OAuth2):
            Response<OAuth2> {
        return try {
            Success(
                athleteApi.getAccessTokenFromRefresh(
                    clientId = TokenConstants.CLIENT_ID,
                    clientSecret = TokenConstants.CLIENT_SECRET,
                    refreshToken = prevOauth.refreshToken,
                    grantType = GRANT_TYPE
                ).apply {
                    /** Refresh token [Bearer] does not include the athlete and thus
                     * id must be SET to prevent an NPE. **/
                    this.athleteId = athleteId
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