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
     * Invoked by the View wishing to transmit a ViewEvent to this ViewModel.
     */
    override fun onEvent(event: TypeOfViewEvent)

    /**
     * Pushes the given ViewState to the StateFlow which the view is observing.
     */
    fun pushState(state: TypeOfViewState)

    /**
     * Navigates to the given Destination if a Router is provided.
     */
    fun routeTo(destination: TypeOfDestination)
}