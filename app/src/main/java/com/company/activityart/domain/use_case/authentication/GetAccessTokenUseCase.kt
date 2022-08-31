package com.company.activityart.domain.use_case.authentication

import android.net.Uri
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import com.company.activityart.util.UriUtils
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val getAccessTokenFromLocalUseCase: GetAccessTokenFromLocalUseCase,
    private val getAccessTokenWithRefreshUseCase: GetAccessTokenWithRefreshUseCase,
    private val getAccessTokenFromRemoteUseCase: GetAccessTokenFromRemoteUseCase,
    private val insertAccessTokenUseCase: InsertAccessTokenUseCase,
    private val uriUtils: UriUtils
) {
    suspend operator fun invoke(uri: Uri? = null): Resource<OAuth2> {

        // If local access token exists, use that
        getAccessTokenFromLocalUseCase()?.let {
            return Success(it)
        }

        // If a URI was provided, read from remote
        val authCode: String? = uri?.let { uriUtils.parseUri(it) }
        return when {
            authCode != null -> getAccessTokenFromRemoteUseCase(authCode)
            else -> getAccessTokenWithRefreshUseCase()
        }.also { if (it is Success) insertAccessTokenUseCase(it.data) }
    }
}