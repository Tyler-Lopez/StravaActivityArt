package com.activityartapp.util.classes

import com.activityartapp.util.enums.ComparisonType
import com.activityartapp.domain.models.Activity
import kotlinx.serialization.Serializable

/** Provides a specification to attempt to match an [Activity] based on its properties
 * and what to color it with if it matches. **/
@Serializable
sealed interface ActivityColorRule {
    val colorArgb: Int

    /** Matches all activities. **/
    @Serializable
    data class Any(override val colorArgb: Int) : ActivityColorRule

    /** Matches any activities which satisfy the [ComparisonType] for the specified [meters]. **/
    @Serializable
    data class Distance(
        override val colorArgb: Int,
        val comparison: ComparisonType,
        val meters: Float
    ) : ActivityColorRule
}