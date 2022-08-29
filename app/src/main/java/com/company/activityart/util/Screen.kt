package com.company.activityart.util

sealed class Screen(
    val route: String
) {
    object Login : Screen("Login")
    object Welcome : Screen("Welcome")
    object About : Screen("About")
    object FilterYear : Screen("FilterYear")
    object FilterMonth : Screen("FilterMonth")
    object FilterType : Screen("FilterType")
    object FilterGear : Screen("FilterGear")
    object FilterDistance : Screen("FilterDistance")
    object VisualizeActivities : Screen("VisualizeActivities")

    fun withArgs(vararg args: String, optionalArgs: Array<Pair<String, String>>? = null): String {
        return buildString {
            append(route)
            args.filter { it.isNotEmpty() }.forEach { arg ->
                append("/$arg")
            }
            optionalArgs?.forEachIndexed { index, pair ->
                append(if (index == 0) '?' else '&')
                    .append(pair.first)
                    .append('=')
                    .append(pair.second)
            }
        }
    }

    override fun toString(): String = route
}
