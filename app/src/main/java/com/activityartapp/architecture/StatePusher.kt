package com.activityartapp.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

interface ViewState

interface StatePusher<TypeOfViewState: ViewState> {

    @Composable
    fun collectedViewState(block: TypeOfViewState.() -> Unit) {
        viewState.collectAsState().value?.apply { block(this) }
    }

    val viewState: StateFlow<TypeOfViewState?>
    fun TypeOfViewState.push()
}