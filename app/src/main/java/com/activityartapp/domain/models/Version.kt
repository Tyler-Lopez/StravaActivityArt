package com.activityartapp.domain.models

interface Version {
    val isLatest: Boolean
    val isSupported: Boolean
}