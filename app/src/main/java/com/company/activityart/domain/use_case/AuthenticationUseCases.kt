package com.company.activityart.domain.use_case

import com.company.activityart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.get_access_token.GetAccessTokenUseCase
import com.company.activityart.domain.use_case.set_access_token.SetAccessTokenUseCase

data class AuthenticationUseCases(
    val getAccessTokenUseCase: GetAccessTokenUseCase,
    val setAccessTokenUseCase: SetAccessTokenUseCase,
    val clearAccessTokenUseCase: ClearAccessTokenUseCase
)
