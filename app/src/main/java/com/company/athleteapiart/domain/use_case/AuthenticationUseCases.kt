package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.get_access_token.GetAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.get_access_token_refresh.GetAccessTokenRefreshUseCase

data class AuthenticationUseCases(
    val getAccessTokenUseCase: GetAccessTokenUseCase,
    val getAccessTokenFromRefreshUseCase: GetAccessTokenRefreshUseCase
)
