package com.company.activityart.architecture

interface ChildViewModel<TypeOfParentViewEvent : ViewEvent> {
    fun attachParent(eventReceiver: EventReceiver<TypeOfParentViewEvent>)
}