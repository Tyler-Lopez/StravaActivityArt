package com.company.activityart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OAuth2Entity(
    @PrimaryKey
    val athleteId: Long,
    val receivedOn: Int,
    val accessToken: String,
    val refreshToken: String
)
