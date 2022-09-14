package com.company.activityart.architecture

/**
 * Blueprint for a ViewModel which can have a [Router] attached as a
 * reference which it may invoke [routeTo] to navigate to a [Destination].
 */
interface RoutingViewModel<TypeOfDestination : Destination> {

    /**
     * Provides a [Router] for the [RoutingViewModel] to [routeTo]]
     */
    fun attachRouter(router: Router<TypeOfDestination>)

    /**
     * Invoked internally by the implementing ViewModel.
     * This function should be used if any operations should not be
     * started until a [Router] has been attached.
     */
    fun onRouterAttached()

    /**
     * Navigates to the given Destination if a Router is provided.
     */
    fun routeTo(destination: TypeOfDestination)
}