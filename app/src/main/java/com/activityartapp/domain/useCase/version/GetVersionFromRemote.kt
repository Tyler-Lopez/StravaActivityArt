package com.activityartapp.domain.useCase.version

import com.activityartapp.domain.VersionRepository
import com.activityartapp.domain.models.Version
import com.activityartapp.util.Response
import com.activityartapp.util.constants.StringConstants.APP_VERSION
import javax.inject.Inject

/** Determines whether the currently-installed [Version] of the client application
 * is the latest and if it is supported. **/
class GetVersionFromRemote @Inject constructor(private val versionRepository: VersionRepository) {
    suspend operator fun invoke(): Response<Version> {
        return versionRepository.getVersion(APP_VERSION)
    }
}