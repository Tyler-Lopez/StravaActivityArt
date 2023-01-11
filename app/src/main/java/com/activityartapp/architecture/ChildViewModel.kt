package com.activityartapp.activityart.architecture

import com.activityartapp.architecture.EventReceiver
import com.activityartapp.architecture.ViewEvent

/**
 * Blueprint for a ViewModel associated with a View that is nested
 * within another View which itself has an associated [EventReceiver]
 * implementing ViewModel.
 *
 * Contains a reference to a parent ViewModel, and may send [TypeOfParentViewEvent]
 * to the parent via [onParentEvent].
 */
interface ChildViewModel<TypeOfParentViewEvent : ViewEvent> {
    fun attachParent(eventReceiver: EventReceiver<TypeOfParentViewEvent>)
    fun onParentEvent(event: TypeOfParentViewEvent)
}