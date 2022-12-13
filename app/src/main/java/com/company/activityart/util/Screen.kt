package com.company.activityart.util

import android.graphics.Bitmap
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    object Login : Screen("Login")
    object Welcome : Screen("Welcome")
    object About : Screen("About")
    object LoadActivities : Screen("LoadActivities")
    object EditArt : Screen("EditArt")
    object SaveArt : Screen("SaveArt")

    fun buildRoute(navArgSpecifications: List<NavArgSpecification>? = null): String {
        return buildString {
            append(route)
            navArgSpecifications?.forEachIndexed { index, spec ->
                append(if (index == 0) '?' else '&')
                    .append(spec.route)
            }
        }
    }

    fun withArgs(vararg args: Pair<String, String>): String {
        return buildString {
            append(route)
            args.forEachIndexed { index, pair ->
                append(if (index == 0) '?' else '&')
                    .append(pair.first)
                    .append('=')
                    .append(pair.second)
            }
        }
    }

    override fun toString(): String = route
}
