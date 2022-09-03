package com.company.activityart.util.ext

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.swipingInOutComposable(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    val animationDurationMs = 500

    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.swipingOutComposable(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    val animationDurationMs = 500

    composable(
        route = route,
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        },
        content = content
    )
}