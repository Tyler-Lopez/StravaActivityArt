package com.activityartapp.domain.use_case.version

import com.activityartapp.domain.VersionRepository
import com.activityartapp.domain.models.Version
import com.activityartapp.util.Response
import com.activityartapp.util.constants.StringConstants.APP_VERSION
import javax.inject.Inject

class GetVersion @Inject constructor(private val versionRepository: VersionRepository) {
    suspend operator fun invoke(): Response<Version> {
        return versionRepository.getVersion(APP_VERSION)
    }
}