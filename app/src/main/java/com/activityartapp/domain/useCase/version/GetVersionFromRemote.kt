package com.activityartapp.domain.useCase.version

import com.activityartapp.BuildConfig
import com.activityartapp.domain.repository.VersionRepository
import com.activityartapp.domain.models.Version
import com.activityartapp.util.Response
import javax.inject.Inject

/** Determines whether the currently-installed [Version] of the client application
 * is the latest and if it is supported. **/
class GetVersionFromRemote @Inject constructor(private val versionRepository: VersionRepository) {
    suspend operator fun invoke(): Response<Version> {
        return versionRepository.getVersion(BuildConfig.VERSION_NAME)
    }
}