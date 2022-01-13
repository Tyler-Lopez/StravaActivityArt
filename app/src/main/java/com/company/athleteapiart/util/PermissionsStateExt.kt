package com.company.athleteapiart.util

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

// https://www.youtube.com/watch?v=ltHN50BdDc4
// How to Do PROPER Permission Handling in Jetpack Compose - Android Studio Tutorial
@ExperimentalPermissionsApi
fun PermissionState.isPermaDenied(): Boolean {
    return !shouldShowRationale && !hasPermission
}