package com.activityartapp.domain.models

import java.util.concurrent.TimeUnit

private const val EXPIRE_BUFFER_SECONDS = 1800

val Athlete.requiresRefresh: Boolean
    get() {
        println("Determining if access token $this requires refresh.")
        val currSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        println("--> currSeconds: $currSeconds")
        println("--> expiresAtUnixSeconds: ${expiresAtUnixSeconds}")
        return expiresAtUnixSeconds < currSeconds + EXPIRE_BUFFER_SECONDS
    }