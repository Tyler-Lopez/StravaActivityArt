package com.company.activityart.architecture

interface EventReceiver<T: ViewEvent> {
    fun onEvent(event: T)
}