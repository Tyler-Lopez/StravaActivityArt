package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.entities.OAuth2Entity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.data.remote.responses.Bearer
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.domain.models.requiresRefresh
import com.company.activityart.util.Response
import com.company.activityart.util.Response.*
import com.company.activityart.util.constants.TokenConstants
import java.net.UnknownHostException
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
                    onRequiresRefresh(refreshToken, athleteId)
                }
                else -> {
                    println("RESULT: SUCCESS")
                    Success(this)
                }
            }
        }
    }

    private suspend fun onRequiresRefresh(refreshToken: String, athleteId: Long):
            Response<OAuth2> {
        return try {
            Success(
                athleteApi.getAccessTokenFromRefresh(
                    clientId = TokenConstants.CLIENT_ID,
                    clientSecret = TokenConstants.CLIENT_SECRET,
                    refreshToken = refreshToken,
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
            Error(exception = e)
        }
    }
}