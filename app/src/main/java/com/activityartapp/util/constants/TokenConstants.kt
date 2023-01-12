package com.activityartapp.util.constants

import android.net.Uri

object TokenConstants {
    const val STRAVA_BASE_AUTH_URL = "https://www.strava.com/oauth/mobile/authorize"
    const val CLIENT_ID_QUERY = "client_id"
    const val REDIRECT_URI_QUERY = "redirect_uri"
    const val RESPONSE_TYPE_QUERY = "response_type"
    const val APPROVAL_PROMPT_QUERY = "approval_prompt"
    const val SCOPE_QUERY = "scope"

    const val CLIENT_ID = 75992
    const val CLIENT_ID_STRING = CLIENT_ID.toString()
    const val CLIENT_SECRET = "1393501e79b0abb641468057d790b6df970771f8"
    const val REDIRECT_URI = "com.activityartapp://myapp.com"
    const val RESPONSE_TYPE = "code"
    const val APPROVAL_PROMPT = "auto"
    const val SCOPE = "activity:read,activity:read_all"

    val authUri: Uri =
        Uri.parse(STRAVA_BASE_AUTH_URL)
            .buildUpon()
            .appendQueryParameter(CLIENT_ID_QUERY, CLIENT_ID_STRING)
            .appendQueryParameter(REDIRECT_URI_QUERY, REDIRECT_URI)
            .appendQueryParameter(RESPONSE_TYPE_QUERY, RESPONSE_TYPE)
            .appendQueryParameter(APPROVAL_PROMPT_QUERY, APPROVAL_PROMPT)
            .appendQueryParameter(SCOPE_QUERY, SCOPE)
            .build()
}