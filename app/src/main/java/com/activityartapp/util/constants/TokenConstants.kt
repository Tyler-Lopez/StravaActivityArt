package com.activityartapp.util.constants

import android.net.Uri

object TokenConstants {
    private const val STRAVA_BASE_AUTH_URL = "https://www.strava.com/oauth/mobile/authorize"
    private const val CLIENT_ID_QUERY = "client_id"
    private const val REDIRECT_URI_QUERY = "redirect_uri"
    private const val RESPONSE_TYPE_QUERY = "response_type"
    private const val APPROVAL_PROMPT_QUERY = "approval_prompt"
    private const val SCOPE_QUERY = "scope"

    const val CLIENT_ID = 75992
    private const val CLIENT_ID_STRING = CLIENT_ID.toString()
    private const val REDIRECT_URI = "com.activityartapp://myapp.com"
    private const val RESPONSE_TYPE = "code"
    private const val APPROVAL_PROMPT = "auto"
    private const val SCOPE = "activity:read,activity:read_all"

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