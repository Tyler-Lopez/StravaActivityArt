package com.company.activityart.architecture

import kotlinx.coroutines.flow.StateFlow

interface RoutingViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent,
        TypeOfDestination : Destination> :
    ViewEventListener<TypeOfViewEvent>, ViewStateSender<TypeOfViewState> {

    override val viewState: StateFlow<TypeOfViewState?>

    /**
     * Provides a [Router] for the [RoutingViewModel] to [routeTo]]
     */
    fun attachRouter(router: Router<TypeOfDestination>)

    /**
     * Invoked internally when a [Router] has been attached.
     * Any functions which require a [Router] should be called no earlier than this.
     */
    fun onRouterAttached()

    /**
     * Invoked by the View wishing to transmit a ViewEvent to this ViewModel.
     */
    override fun onEvent(event: TypeOfViewEvent)

    /**
     * Invoked by the View wishing to transmit a debounced ViewEvent to this ViewModel.
     * This is useful for events that should only occur once in a short period of time
     * and where, for example, rapidly clicking a button might open undesirable screens.
     */
    override fun onEventDebounced(event: TypeOfViewEvent)

    /**
     * Pushes the given ViewState to the StateFlow which the view is observing.
     */
    fun pushState(state: TypeOfViewState)

    /**
     * Navigates to the given Destination if a Router is provided.
     */
    fun routeTo(destination: TypeOfDestination)
}