package com.company.activityart.domain.models

import com.company.activityart.presentation.editArtScreen.Resolution

fun interface ResolutionListFactory {
    fun create(): MutableList<Resolution>
}