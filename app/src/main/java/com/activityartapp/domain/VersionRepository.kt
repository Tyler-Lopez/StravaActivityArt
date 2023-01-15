package com.activityartapp.domain

import com.activityartapp.util.Response

interface VersionRepository {
    suspend fun newVersionAvailable(currMajor: Int, currMinor: Int): Response<Boolean>
}