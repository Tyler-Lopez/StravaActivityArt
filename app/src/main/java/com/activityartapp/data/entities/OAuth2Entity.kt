package com.activityartapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.models.OAuth2

@Entity
data class OAuth2Entity(
    @PrimaryKey
    override val athleteId: Long,
    override val expiresAtUnixSeconds: Int,
    override val accessToken: String,
    override val refreshToken: String
) : OAuth2 {
}
