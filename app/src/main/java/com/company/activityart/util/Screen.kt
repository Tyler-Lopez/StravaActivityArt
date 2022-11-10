package com.company.activityart.util

import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    companion object {
        private const val ATHLETE_ID_ARG = "athleteId"
        private const val ACCESS_TOKEN_ARG = "accessToken"
        const val ATHLETE_ACCESS_ARGS =
            "?$ATHLETE_ID_ARG={$ATHLETE_ID_ARG}&$ACCESS_TOKEN_ARG={$ACCESS_TOKEN_ARG}"

        val ATHLETE_ID_NAV_ARG = navArgument(
            name = ATHLETE_ID_ARG
        ) {
            type = StringType
        }
    }

    object Login : Screen("Login")
    object Welcome : Screen("Welcome")
    object About : Screen("About")
    object LoadActivities : Screen("LoadActivities")
    object EditArt : Screen("EditArt")
    object SaveArt : Screen("SaveArt")

    fun withArgs(args: Array<Pair<String, String>>? = null): String {
        return buildString {
            append(route)
            args?.forEachIndexed { index, pair ->
                append(if (index == 0) '?' else '&')
                    .append(pair.first)
                    .append('=')
                    .append(pair.second)
            }
        }
    }

    override fun toString(): String = route
}
