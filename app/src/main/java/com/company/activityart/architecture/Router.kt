package com.company.activityart.architecture

interface Router<T: Destination> {
    fun navigateTo(destination: T)
}