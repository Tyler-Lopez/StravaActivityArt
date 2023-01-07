package com.company.activityart.util

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NavArgSpecification {

    companion object {
        private const val ACTIVITY_TYPES_KEY = "activityTypes"
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val COLOR_ACTIVITIES_KEY = "colorActivities"
        private const val COLOR_BACKGROUND_KEY = "colorBackground"
        private const val COLOR_FONT_KEY = "colorFont"
        private const val FILTER_DATE_AFTER_MS_KEY = "filterDateAfterMs"
        private const val FILTER_DATE_BEFORE_MS_KEY = "filterDateBeforeMs"
        private const val FILTER_DISTANCE_LESS_THAN_KEY = "filterDistanceLessThan"
        private const val FILTER_DISTANCE_MORE_THAN_KEY = "filterDistanceMoreThan"
        private const val SIZE_HEIGHT_KEY = "sizeHeight"
        private const val SIZE_WIDTH_KEY = "sizeWidth"
        private const val STROKE_WIDTH_KEY = "strokeWidth"
        private const val TEXT_LEFT_KEY = "textLeft"
        private const val TEXT_CENTER_KEY = "textCenter"
        private const val TEXT_RIGHT_KEY = "textRight"
        private const val TEXT_FONT_KEY = "textFont"
        private const val TEXT_FONT_SIZE_KEY = "textFontSize"

        private const val ERROR_MISSING_KEY = "ERROR: Missing key [%s] in saved state handle."
    }

    val name: String

    val key: String
        get() = navArg.name

    val navArg: NamedNavArgument
        get() = navArgument(name = name) { type = NavType.StringType }

    val route: String
        get() = "$key={$key}"

    fun rawArg(handle: SavedStateHandle): String {
        return handle[key] ?: error(ERROR_MISSING_KEY.format(key))
    }

    object ActivityTypes : NavArgSpecification {
        override val name = ACTIVITY_TYPES_KEY
    }

    object AthleteId : NavArgSpecification {
        override val name = ATHLETE_ID_KEY
    }

    object AccessToken : NavArgSpecification {
        override val name = ACCESS_TOKEN_KEY
    }

    object ColorActivitiesArgb : NavArgSpecification {
        override val name = COLOR_ACTIVITIES_KEY
    }

    object ColorBackgroundArgb : NavArgSpecification {
        override val name = COLOR_BACKGROUND_KEY
    }

    object ColorFontArgb : NavArgSpecification {
        override val name: String = COLOR_FONT_KEY
    }

    object FilterDateAfterMs : NavArgSpecification {
        override val name = FILTER_DATE_AFTER_MS_KEY
    }

    object FilterDateBeforeMs : NavArgSpecification {
        override val name = FILTER_DATE_BEFORE_MS_KEY
    }

    object FilterDistanceLessThan : NavArgSpecification {
        override val name: String = FILTER_DISTANCE_LESS_THAN_KEY
    }

    object FilterDistanceMoreThan : NavArgSpecification {
        override val name: String = FILTER_DISTANCE_MORE_THAN_KEY
    }

    object StrokeWidth : NavArgSpecification {
        override val name = STROKE_WIDTH_KEY
    }

    object SizeHeightPx : NavArgSpecification {
        override val name = SIZE_HEIGHT_KEY
    }

    object SizeWidthPx : NavArgSpecification {
        override val name = SIZE_WIDTH_KEY
    }

    object TextLeft : NavArgSpecification {
        override val name: String = TEXT_LEFT_KEY
    }

    object TextCenter : NavArgSpecification {
        override val name: String = TEXT_CENTER_KEY
    }

    object TextRight : NavArgSpecification {
        override val name: String = TEXT_RIGHT_KEY
    }

    object TextFont : NavArgSpecification {
        override val name: String = TEXT_FONT_KEY
    }

    object TextFontSize : NavArgSpecification {
        override val name: String = TEXT_FONT_SIZE_KEY
    }
}

