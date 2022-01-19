package com.company.athleteapiart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OAuth2Entity(
    @PrimaryKey
    val receivedOn: Int,
    val accessToken: String,
    val refreshToken: String
)
