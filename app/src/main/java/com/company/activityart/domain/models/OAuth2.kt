package com.company.activityart.domain.models

interface OAuth2 {
    val athleteId: Long
    val expiresAtUnixSeconds: Int
    val accessToken: String
    val refreshToken: String
}