package com.company.activityart.util

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.LongType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.navArgument
import com.company.activityart.presentation.editArtScreen.StrokeWidthType

sealed interface NavArg {

    companion object {
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val COLOR_ACTIVITIES_KEY = "colorActivities"
        private const val COLOR_BACKGROUND_KEY = "colorBackground"
        private const val STROKE_WIDTH_KEY = "strokeWidth"

        val athleteId = NavArgSpecification(ATHLETE_ID_KEY, LongType)
        val accessToken = NavArgSpecification(ACCESS_TOKEN_KEY, StringType)
        val colorActivities = NavArgSpecification(COLOR_ACTIVITIES_KEY, StringType)
        val colorBackground = NavArgSpecification(COLOR_BACKGROUND_KEY, StringType)
        val strokeWidth = NavArgSpecification(STROKE_WIDTH_KEY, StringType)
    }

    val navArg: NamedNavArgument

    data class NavArgSpecification(
        val name: String,
        val type: NavType<*>
    ) : NavArg {

        override val navArg: NamedNavArgument = navArgument(name = name) { this.type = type }

        val key: String = navArg.name
        val route: String = "$key={$key}"
    }
}
