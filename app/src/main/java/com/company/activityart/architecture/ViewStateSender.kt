package com.company.activityart.architecture

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

interface ViewState

interface ViewStateSender<TypeOfViewState: ViewState> {
    val viewState: StateFlow<TypeOfViewState?>
    fun TypeOfViewState.push()
}