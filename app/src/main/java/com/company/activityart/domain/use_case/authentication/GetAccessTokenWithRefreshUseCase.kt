package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.entities.OAuth2Entity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.data.remote.responses.Bearer
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.domain.models.requiresRefresh
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import com.company.activityart.util.TokenConstants
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

    suspend operator fun invoke(): Resource<OAuth2> {
        getAccessTokenFromLocalUseCase().apply {
            return when {
                this == null -> Error()
                requiresRefresh -> onRequiresRefresh(refreshToken)
                else -> Success(this)
            }
        }
    }

    private suspend fun onRequiresRefresh(refreshToken: String):
            Resource<OAuth2> {
        return try {
            Success(
                athleteApi.getAccessToken(
                    clientId = TokenConstants.CLIENT_ID,
                    clientSecret = TokenConstants.CLIENT_SECRET,
                    code = refreshToken,
                    grantType = GRANT_TYPE
                )
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