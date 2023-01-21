package com.activityartapp.domain.models

interface OAuth2WithoutAthlete {
    val expiresAtUnixSeconds: Int
    val accessToken: String
    val refreshToken: String
}