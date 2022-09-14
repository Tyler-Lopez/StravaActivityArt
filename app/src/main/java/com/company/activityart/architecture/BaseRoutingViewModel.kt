package com.company.activityart.architecture

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

    final override fun routeTo(destination: TypeOfDestination) {
        router?.routeTo(destination)
    }
}