package com.activityartapp.domain.models

interface Athlete : OAuth2 {
    val athleteId: Long
    val lastCachedUnixMs: Long?
    override val expiresAtUnixSeconds: Int
    override val accessToken: String
    override val refreshToken: String
}
