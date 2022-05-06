package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.get_access_token.GetAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.set_access_token.SetAccessTokenUseCase

data class AuthenticationUseCases(
    val getAccessTokenUseCase: GetAccessTokenUseCase,
    val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    val setAccessTokenUseCase: SetAccessTokenUseCase
)
