package com.company.activityart.presentation.visualize_screen

sealed class ResolutionSpec(
    val display: String,
    val resolution: Pair<Float, Float>
) {
    object LinkedInCover : ResolutionSpec("LinkedIn Cover", Pair(1584f, 396f))
}