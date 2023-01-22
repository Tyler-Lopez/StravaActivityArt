package com.activityartapp.util

import android.net.Uri

class UriUtils {

    /**
     * Parses an Intent Uri to retrieve the authorization code.
     *
     * On Nexus 7 API 30 when redirecting from Browser (Strava not installed)
     * The URI is com.activityartapp://myapp.com?state=&code=9068207431c5f9ac453fc06d1ba7091e8b47e2d5&scope=read,activity:read,activity:read_all
     * @return Parsed URI, or null if invalid.
     */
    fun parseUri(uri: Uri): String = uri
        .toString()
        .run {
            substring(
                startIndex = indexOf("code=") + 5,
                endIndex = indexOf("&scope=")
            )
        }
}