package com.activityartapp.architecture

interface Destination

interface Router<TypeOfDestination: Destination> {
    fun routeTo(destination: TypeOfDestination)
}