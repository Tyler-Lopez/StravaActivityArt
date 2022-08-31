package com.company.activityart.architecture

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseRoutingViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent: ViewEvent,
        TypeOfDestination : Destination>
    : ViewModel(), RoutingViewModel<TypeOfViewState, TypeOfViewEvent, TypeOfDestination> {

    private var router: Router<TypeOfDestination>? = null
    private var _viewState: MutableStateFlow<TypeOfViewState?> = MutableStateFlow(null)
    final override val viewState: StateFlow<TypeOfViewState?> = _viewState

    final override fun attachRouter(router: Router<TypeOfDestination>) {
        this.router = router
    }

    final override fun routeTo(destination: TypeOfDestination) {
        router?.routeTo(destination)
    }

    final override fun pushState(state: TypeOfViewState) {
        _viewState.value = state
    }

}