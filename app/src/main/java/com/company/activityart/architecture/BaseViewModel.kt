package com.company.activityart.architecture

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

abstract class BaseViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent>
    : ViewModel(), EventReceiverStatePusherViewModel<TypeOfViewState, TypeOfViewEvent> {

    companion object {
        /** A debounced event may be invoked no more frequently than this time. */
        private const val DEBOUNCE_TIME_MS = 1000L
    }

    private var lastDebouncedMs: Long = 0L

    private var _viewState: MutableStateFlow<TypeOfViewState?> = MutableStateFlow(null)

    val lastPushedState: TypeOfViewState?
        get() = viewState.value

    final override val viewState: StateFlow<TypeOfViewState?> = _viewState

    final override fun pushState(state: TypeOfViewState) {
        _viewState.value = state
    }

    final override fun TypeOfViewState.push() {
        pushState(this)
    }

    final override fun onEventDebounced(event: TypeOfViewEvent) {
        val currTime = System.currentTimeMillis()
        if (currTime > lastDebouncedMs + DEBOUNCE_TIME_MS) {
            lastDebouncedMs = currTime
            onEvent(event)
        }
    }
}