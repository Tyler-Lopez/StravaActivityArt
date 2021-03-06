package com.company.activityart.util

enum class HTTPFault {
    UNAUTHORIZED, UNKNOWN;

    companion object {
        fun getEnum(value: String?) = when (value) {
            "HTTP 401" -> UNAUTHORIZED
            else -> UNKNOWN
        }
    }
}