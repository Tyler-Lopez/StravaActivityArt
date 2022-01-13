package com.company.athleteapiart.util

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@ExperimentalPermissionsApi
fun PermissionState.isPermaDenied(): Boolean {
    return !shouldShowRationale && !hasPermission
}