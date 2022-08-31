package com.company.activityart.architecture

interface Router<TypeOfDestination: Destination> {
    fun routeTo(destination: TypeOfDestination)
}