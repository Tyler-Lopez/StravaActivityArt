package com.activityartapp.util.classes

import com.activityartapp.domain.models.Activity
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.util.enums.ComparisonType
import com.activityartapp.util.enums.SportType
import kotlinx.serialization.Serializable

/** Provides a specification to attempt to match an [Activity] based on its properties
 * and what to color it with if it matches. **/
@Serializable
sealed interface ActivityColorRule {
    val color: ColorWrapper

    /** Matches all activities. **/
    @Serializable
    data class Any(override val color: ColorWrapper) : ActivityColorRule

    /** Matches any activities which satisfy the [ComparisonType] for the specified [meters]. **/
    /* todo add back
    @Serializable
    data class Distance(
        override val color: ColorWrapper,
        val comparison: ComparisonType,
        val meters: Float
    ) : ActivityColorRule
     */

    @Serializable
    data class Type(
        override val color: ColorWrapper,
        val type: SportType?
    ) : ActivityColorRule
}