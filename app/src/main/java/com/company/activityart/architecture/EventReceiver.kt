package com.company.activityart.architecture

interface EventReceiver<T: Event> {
    fun onEvent(event: T)
}