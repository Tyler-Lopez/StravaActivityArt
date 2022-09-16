package com.company.activityart.util

import android.net.Uri

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

}