package com.company.activityart.architecture

import androidx.compose.runtime.State

interface StateSender<T: ViewState> {
    val viewState: State<T>
}