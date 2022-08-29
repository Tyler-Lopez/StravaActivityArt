package com.company.activityart.domain.use_case.authentication

import com.company.activityart.domain.models.OAuth2
import com.company.activityart.domain.models.requiresRefresh
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import javax.inject.Inject

class GetAccessTokenWithRefreshUseCase @Inject constructor(
    private val getAccessTokenFromLocalUseCase: GetAccessTokenFromLocalUseCase,
    private val getAccessTokenFromRemoteRefreshUseCase: GetAccessTokenFromRemoteUseCase
) {
    suspend operator fun invoke(): Resource<OAuth2> {
        getAccessTokenFromLocalUseCase().apply {
            return when {
                this == null -> Error()
                requiresRefresh -> getAccessTokenFromRemoteRefreshUseCase(refreshToken)
                else -> Success(this)
            }
        }
    }
}