package com.company.activityart.util

import androidx.navigation.NavType

private const val ATHLETE_ID_KEY = "athleteId"
private const val ACCESS_TOKEN_KEY = "accessToken"
private const val COLOR_ACTIVITIES_KEY = "colorActivities"
private const val COLOR_BACKGROUND_KEY = "colorBackground"
private const val STROKE_WIDTH_KEY = "strokeWidth"
private const val SIZE_WIDTH_KEY = "sizeWidth"
private const val SIZE_HEIGHT_KEY = "sizeHeight"

val athleteIdNavSpec = NavArgSpecification(ATHLETE_ID_KEY, NavType.LongType)
val accessTokenNavSpec = NavArgSpecification(ACCESS_TOKEN_KEY, NavType.StringType)
val colorActivities = NavArgSpecification(COLOR_ACTIVITIES_KEY, NavType.StringType)
val colorBackground = NavArgSpecification(COLOR_BACKGROUND_KEY, NavType.StringType)
val strokeWidth = NavArgSpecification(STROKE_WIDTH_KEY, NavType.StringType)
val sizeHeight = NavArgSpecification(SIZE_HEIGHT_KEY, NavType.IntType)
val sizeWidth = NavArgSpecification(SIZE_WIDTH_KEY, NavType.IntType)