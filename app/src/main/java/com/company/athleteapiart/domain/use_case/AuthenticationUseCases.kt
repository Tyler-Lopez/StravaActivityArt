package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.get_set_access_token.GetAccessTokenUseCase

data class AuthenticationUseCases(
    val getAccessTokenUseCase: GetAccessTokenUseCase,
    val clearAccessTokenUseCase: ClearAccessTokenUseCase,
)
