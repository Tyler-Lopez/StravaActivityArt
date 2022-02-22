package com.company.athleteapiart.util

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.error_noactivities_screen.ErrorNoActivitiesScreen
import com.google.accompanist.navigation.animation.*

@ExperimentalAnimationApi
public fun NavGraphBuilder.noAnimationComposable(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        content()
    }
}