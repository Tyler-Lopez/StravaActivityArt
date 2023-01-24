package com.activityartapp.domain.repository

import com.activityartapp.domain.models.Version
import com.activityartapp.util.Response

interface VersionRepository {
    suspend fun getVersion(versionStr: String): Response<Version>
}