package com.company.activityart.util

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavArg(val navArg: NamedNavArgument) {

    companion object {
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val ACCESS_TOKEN_KEY = "accessToken"
    }

    object AthleteId : NavArg(navArgument(name = ATHLETE_ID_KEY) {
        type = NavType.LongType
    })
    object AccessToken : NavArg(navArgument(name = ACCESS_TOKEN_KEY) {
        type = NavType.StringType
    })

    val key: String = navArg.name
    val route: String = "$key={$key}"
}
