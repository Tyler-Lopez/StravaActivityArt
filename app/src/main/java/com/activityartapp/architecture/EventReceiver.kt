package com.activityartapp.architecture

interface ViewEvent

interface EventReceiver<TypeOfViewEvent: ViewEvent> {
    fun onEvent(event: TypeOfViewEvent)
    fun onEventDebounced(event: TypeOfViewEvent)
}