package com.company.activityart.util

import android.net.Uri
import com.company.activityart.util.TokenConstants.APPROVAL_PROMPT
import com.company.activityart.util.TokenConstants.APPROVAL_PROMPT_QUERY
import com.company.activityart.util.TokenConstants.CLIENT_ID
import com.company.activityart.util.TokenConstants.CLIENT_ID_QUERY
import com.company.activityart.util.TokenConstants.CLIENT_ID_STRING
import com.company.activityart.util.TokenConstants.REDIRECT_URI
import com.company.activityart.util.TokenConstants.REDIRECT_URI_QUERY
import com.company.activityart.util.TokenConstants.RESPONSE_TYPE
import com.company.activityart.util.TokenConstants.RESPONSE_TYPE_QUERY
import com.company.activityart.util.TokenConstants.SCOPE
import com.company.activityart.util.TokenConstants.SCOPE_QUERY
import com.company.activityart.util.TokenConstants.STRAVA_BASE_AUTH_URL

class UriUtils {

    companion object {
        private const val URI_START_INDEX = 43
        private const val URI_DELIMITER_CHAR = '&'
    }

    /**
     * Parses an Intent Uri to retrieve the authorization code, which may be used
     * as the parameter of
     * [com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase]
     * to fetch an [com.company.activityart.domain.models.OAuth2] object from remote.
     *
     * @return Parsed URI, or null if invalid.
     */
    fun parseUri(uri: Uri): String? =
        uri.toString()
            .takeIf { it.lastIndex >= URI_START_INDEX }
            ?.substring(URI_START_INDEX)
            ?.takeIf { it.contains(URI_DELIMITER_CHAR) }
            ?.substringBefore(URI_DELIMITER_CHAR)

    val authenticationUri: Uri =
        Uri.parse(STRAVA_BASE_AUTH_URL)
            .buildUpon()
            .appendQueryParameter(CLIENT_ID_QUERY, CLIENT_ID_STRING)
            .appendQueryParameter(REDIRECT_URI_QUERY, REDIRECT_URI)
            .appendQueryParameter(RESPONSE_TYPE_QUERY, RESPONSE_TYPE)
            .appendQueryParameter(APPROVAL_PROMPT_QUERY, APPROVAL_PROMPT)
            .appendQueryParameter(SCOPE_QUERY, SCOPE)
            .build()
}