package com.company.activityart.architecture

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.System.currentTimeMillis

abstract class BaseRoutingViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent,
        TypeOfDestination : Destination> : ViewModel(), RoutingViewModel<TypeOfViewState, TypeOfViewEvent, TypeOfDestination> {

    companion object {
        /** A debounced event may be invoked no more frequently than this time. */
        private const val DEBOUNCE_TIME_MS = 1000L
    }

    private var router: Router<TypeOfDestination>? = null
    private var lastDebouncedMs: Long = 0L

    private var _viewState: MutableStateFlow<TypeOfViewState?> = MutableStateFlow(null)

    final override val viewState: StateFlow<TypeOfViewState?> = _viewState

    final override fun attachRouter(router: Router<TypeOfDestination>) {
        this.router = router
        onRouterAttached()
    }

    final override fun routeTo(destination: TypeOfDestination) {
        router?.routeTo(destination)
    }

    final override fun pushState(state: TypeOfViewState) {
        _viewState.value = state
    }

    final override fun onEventDebounced(event: TypeOfViewEvent) {
        val currTime = currentTimeMillis()
        if (currTime > lastDebouncedMs + DEBOUNCE_TIME_MS) {
            lastDebouncedMs = currTime
            onEvent(event)
        }
    }

}