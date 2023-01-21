package com.activityartapp.data.remote.responses

import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.models.OAuth2WithoutAthlete
import com.google.gson.annotations.SerializedName

data class BearerWithoutAthlete(
    @SerializedName("access_token")
    override val accessToken: String,
    @SerializedName("expires_at")
    override val expiresAtUnixSeconds: Int,
    @SerializedName("refresh_token")
    override val refreshToken: String,
    val expires_in: Int,
    val token_type: String
) : OAuth2WithoutAthlete
