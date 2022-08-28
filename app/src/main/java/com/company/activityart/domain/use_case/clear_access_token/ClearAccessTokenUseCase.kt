package com.company.activityart.domain.use_case.clear_access_token

import android.content.Context

class ClearAccessTokenUseCase {
    suspend fun clearAccessToken(context: Context) {
        val oAuth2Entity = OAuth2Database
            .getInstance(context.applicationContext)
            .oAuth2Dao
            .clearOauth2()
    }
}