package com.company.activityart.architecture

import androidx.compose.runtime.Composable

interface Router<T: Destination> {
    fun routeTo(destination: T)
}