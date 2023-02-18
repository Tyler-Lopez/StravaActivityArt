package com.activityartapp.domain.useCase.authentication

import android.net.Uri
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import com.activityartapp.util.Response
import com.activityartapp.util.UriUtils
import com.activityartapp.util.doOnSuccess
import javax.inject.Inject

/** Retrieves a non-expired access token necessary to retrieve an [Athlete]'s
 *  [Activity] from Strava. We get the access token directly from on disk
 *  storage or by sending an authorization code or an on-disk stored
 *  refresh token to Strava API.
 *
 *  On success, the [Athlete] is automatically inserted into on disk storage. **/
class GetAccessTokenFromDiskOrRemote @Inject constructor(
    private val getAccessTokenWithRefreshUseCase: GetAccessTokenWithRefreshTokenFromRemote,
    private val getAccessTokenFromRemoteUseCase: GetAccessTokenWithAuthorizationCodeFromRemote,
    private val insertAccessTokenIntoDisk: InsertAccessTokenIntoDisk,
    private val uriUtils: UriUtils
) {
    suspend operator fun invoke(uri: Uri? = null): Response<Athlete> {
        /** If URI was provided, parse out authorization code **/
        val authCode: String? = uri?.let { uriUtils.parseUri(it) }
        println("Here, auth code was $authCode")
        return when {
            /** Receive access token from provided auth code **/
            authCode != null -> getAccessTokenFromRemoteUseCase(authCode)
            /** Read from locally-stored access token, refresh if needed **/
            else -> getAccessTokenWithRefreshUseCase()
        }.doOnSuccess {
            println("Here, response is $data")
            insertAccessTokenIntoDisk(data)
        }
    }
}