package com.company.athleteapiart.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OAuth2(
    @PrimaryKey(autoGenerate = false)
    val receivedOn: Int,
    val accessToken: String,
    val refreshToken: String
)
