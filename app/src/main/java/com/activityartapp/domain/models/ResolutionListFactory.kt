package com.activityartapp.domain.models

import com.activityartapp.presentation.editArtScreen.Resolution

fun interface ResolutionListFactory {
    fun create(): MutableList<Resolution>
}