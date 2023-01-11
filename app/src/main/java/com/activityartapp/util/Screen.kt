package com.activityartapp.util

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

    fun withArgs(vararg args: Pair<NavArgSpecification, String>): String {
        return buildString {
            append(route)
            args.forEachIndexed { index, pair ->
                append(if (index == 0) '?' else '&')
                    .append(pair.first.key)
                    .append('=')
                    .append(pair.second)
            }
        }
    }

    override fun toString(): String = route
}
