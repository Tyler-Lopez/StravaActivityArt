package com.activityartapp.domain.use_case.authentication

import android.net.Uri
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.activityart.util.Response
import com.activityartapp.util.UriUtils
import com.activityartapp.activityart.util.doOnError
import com.activityartapp.activityart.util.doOnSuccess
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Retrieves access token from local and / or remote repositories.
 */
class GetAccessTokenUseCase @Inject constructor(
    private val getAccessTokenWithRefreshUseCase: GetAccessTokenWithRefreshUseCase,
    private val getAccessTokenFromRemoteUseCase: GetAccessTokenFromRemoteUseCase,
    private val insertAccessTokenUseCase: InsertAccessTokenUseCase,
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val uriUtils: UriUtils
) {
    suspend operator fun invoke(uri: Uri? = null): Response<OAuth2> {

        /** If URI was provided, parse out authorization code **/
        val authCode: String? = uri?.let { uriUtils.parseUri(it) }

        return when {
            /** Receive access token from provided auth code **/
            authCode != null -> getAccessTokenFromRemoteUseCase(authCode)
            /** Read from locally-stored access token, refresh if needed **/
            else -> getAccessTokenWithRefreshUseCase()
        }
            /** On success, store locally, else on error clear out previous entry
             * Todo, improve this - maybe best to not clear out error **/
            .doOnSuccess {
                println("here $data")
                insertAccessTokenUseCase(data)
            }
            .doOnError {
                println("Here, error $exception")
                /** If the issue isn't because of internet, clear local cache **/
                if (exception !is UnknownHostException) {
                    clearAccessTokenUseCase()
                }
            }
    }
}