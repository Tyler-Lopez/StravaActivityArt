package com.company.activityart.util

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NavArgSpecification {

    companion object {
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val COLOR_ACTIVITIES_KEY = "colorActivities"
        private const val COLOR_BACKGROUND_KEY = "colorBackground"
        private const val STROKE_WIDTH_KEY = "strokeWidth"
        private const val SIZE_WIDTH_KEY = "sizeWidth"
        private const val SIZE_HEIGHT_KEY = "sizeHeight"

        private const val ERROR_MISSING_KEY = "ERROR: Missing key [%s] in saved state handle."
    }

    val name: String
    val navType: NavType<*>

    val key: String
        get() = navArg.name

    val navArg: NamedNavArgument
        get() = navArgument(name = name) { type = navType }

    val route: String
        get() = "$key={$key}"

    fun retrieveArg(handle: SavedStateHandle): Any {
        return handle[key] ?: error(ERROR_MISSING_KEY.format(key))
    }

    object AthleteId : NavArgSpecification {
        override val name = ATHLETE_ID_KEY
        override val navType = NavType.LongType
    }

    object AccessToken : NavArgSpecification {
        override val name = ACCESS_TOKEN_KEY
        override val navType = NavType.StringType
    }
}

