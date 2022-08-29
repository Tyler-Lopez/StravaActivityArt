package com.company.activityart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.activityart.domain.models.OAuth2

@Entity
data class OAuth2Entity(
    @PrimaryKey
    override val athleteId: Long,
    override val expiresAtUnixSeconds: Int,
    override val accessToken: String,
    override val refreshToken: String
) : OAuth2
