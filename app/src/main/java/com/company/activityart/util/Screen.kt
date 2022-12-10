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

    fun withRequiredArgs(args: Array<String>): String {
        return buildString {
            append(route)
            args.forEach {
                append('/')
                    .append(it)
            }
        }
    }

    override fun toString(): String = route
}
