package com.company.activityart.architecture

abstract class BaseChildViewModel<
        TypeOfViewState : ViewState,
        TypeOfViewEvent : ViewEvent,
        TypeOfParentViewEvent : ViewEvent>
    : BaseViewModel<TypeOfViewState, TypeOfViewEvent>(), ChildViewModel<TypeOfParentViewEvent> {

    private var parent: EventReceiver<TypeOfParentViewEvent>? = null

    final override fun attachParent(eventReceiver: EventReceiver<TypeOfParentViewEvent>) {
        parent = eventReceiver
    }

    final override fun onParentEvent(event: TypeOfParentViewEvent) {
        parent?.onEvent(event)
    }
}