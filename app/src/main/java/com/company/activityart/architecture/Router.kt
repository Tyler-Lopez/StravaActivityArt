package com.company.activityart.architecture

import kotlinx.parcelize.Parcelize

interface Destination

interface Router<TypeOfDestination: Destination> {
    fun routeTo(destination: TypeOfDestination)
}