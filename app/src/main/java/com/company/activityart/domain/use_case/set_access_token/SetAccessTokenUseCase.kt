package com.company.activityart.domain.use_case.set_access_token

import android.content.Context
import com.company.activityart.data.database.OAuth2Database
import com.company.activityart.data.entities.OAuth2Entity

class SetAccessTokenUseCase {
    // Invoked to set access token in Room database
    suspend fun setAccessToken(
        context: Context,
        oAuth2Entity: OAuth2Entity
    ) {
        val oAuth2Dao = OAuth2Database
            .getInstance(context.applicationContext)
            .oAuth2Dao

        oAuth2Dao.insertOauth2(oAuth2Entity = oAuth2Entity)
    }
}