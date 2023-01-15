package com.activityartapp.domain.use_case.version

import com.activityartapp.domain.VersionRepository
import com.activityartapp.util.Response
import com.activityartapp.util.constants.StringConstants.VERSION_MAJOR
import com.activityartapp.util.constants.StringConstants.VERSION_MINOR
import javax.inject.Inject

class IsNewVersionAvailable @Inject constructor(private val versionRepository: VersionRepository) {
    suspend operator fun invoke(): Response<Boolean> {
        return versionRepository.newVersionAvailable(
            currMajor = VERSION_MAJOR,
            currMinor = VERSION_MINOR
        )
    }
}