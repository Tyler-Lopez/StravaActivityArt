package com.company.activityart.architecture

import kotlinx.coroutines.flow.StateFlow

/**
 * Blueprint for a ViewModel which can receive [ViewEvent] in the form of
 * the [onEvent] / [onEventDebounced] methods and push [ViewState] via.
 * [pushState].
 */
interface EventReceiverStatePusherViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent> :
    EventReceiver<TypeOfViewEvent>, StatePusher<TypeOfViewState> {

    override val viewState: StateFlow<TypeOfViewState?>

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

}