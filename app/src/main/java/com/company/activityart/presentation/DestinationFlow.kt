package com.company.activityart.presentation

import kotlinx.coroutines.flow.MutableStateFlow

class DestinationFlow {
    var destinations = MutableStateFlow<MainDestination?>(null)
}