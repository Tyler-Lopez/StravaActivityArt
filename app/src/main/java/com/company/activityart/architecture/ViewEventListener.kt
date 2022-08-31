package com.company.activityart.architecture

interface ViewEvent

interface ViewEventListener<TypeOfViewEvent: ViewEvent> {
    fun onEvent(event: TypeOfViewEvent)
}