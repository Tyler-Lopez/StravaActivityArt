package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.get_access_token.GetAccessTokenUseCase

data class AuthenticationUseCases(
    val getAccessTokenUseCase: GetAccessTokenUseCase,
)
