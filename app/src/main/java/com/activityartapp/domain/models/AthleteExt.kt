package com.activityartapp.domain.models

import java.util.concurrent.TimeUnit

private const val DATA_EXPIRY_DAYS = 3
private const val SECONDS_IN_DAY = 86400

/* commented for now following removal of athlete get
val Athlete.secondsSinceReceived: Int
    get() {
        val currSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
        return currSeconds - (receivedOnUnixSeconds ?: currSeconds)
    }

 */

/*
// This is important as Strava does not want you caching data for more than 3 days
val Athlete.dataExpired: Boolean
    get() {
        val expirationSeconds = SECONDS_IN_DAY * DATA_EXPIRY_DAYS
        return secondsSinceReceived > expirationSeconds
    }

val Athlete.fullName: String
    get() = "$firstName $lastName"

 */