package com.company.activityart.domain.models

import java.util.concurrent.TimeUnit

private const val EXPIRE_BUFFER_SECONDS = 1800

val OAuth2.requiresRefresh: Boolean
    get() {
        val currSeconds = TimeUnit.SECONDS.toMillis(System.currentTimeMillis())
        return expiresAtUnixSeconds < currSeconds + EXPIRE_BUFFER_SECONDS
    }