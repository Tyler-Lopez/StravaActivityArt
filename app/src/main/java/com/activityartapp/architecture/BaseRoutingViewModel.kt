package com.activityartapp.architecture

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRoutingViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent,
        TypeOfDestination : Destination>
    : BaseViewModel<TypeOfViewState, TypeOfViewEvent>(), RoutingViewModel<TypeOfDestination> {

    private var router: Router<TypeOfDestination>? = null

    final override fun attachRouter(router: Router<TypeOfDestination>) {
        this.router = router
        onRouterAttached()
    }

    override fun onRouterAttached() {
        // No-op unless implemented
    }

    final override suspend fun routeTo(destination: TypeOfDestination) {
        withContext(Dispatchers.Main.immediate) {
            router?.routeTo(destination)
        }
    }
}