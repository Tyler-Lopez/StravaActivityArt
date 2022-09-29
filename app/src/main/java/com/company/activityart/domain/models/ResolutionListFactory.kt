package com.company.activityart.domain.models

import com.company.activityart.presentation.edit_art_screen.Resolution

fun interface ResolutionListFactory {
    fun create(): MutableList<Resolution>
}