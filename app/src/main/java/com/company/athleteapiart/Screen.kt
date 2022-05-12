package com.company.athleteapiart

sealed class Screen(
    val route: String
) {
    object TimeSelect : Screen("TimeSelect")
    object Login : Screen("Login")
    object Welcome : Screen("Welcome")
    object About : Screen("About")
    object FilterMonth : Screen("FilterMonth")
    object FilterType : Screen("FilterType")
    object FilterGear : Screen("FilterGear")
    object FilterDistance : Screen("FilterDistance")

    object FilterActivities : Screen("FilterActivities")
    object FormatActivitiesOne : Screen("FormatActivitiesOne")
    object FormatActivitiesTwo : Screen("FormatActivitiesTwo")
    object FormatActivitiesThree : Screen("FormatActivitiesThree")
    object FormatActivitiesFour : Screen("FormatActivitiesFour")
    object ErrorNoActivities : Screen("ErrorNoActivities")
    object VisualizeActivities : Screen("VisualizeActivities")
    object SaveImage : Screen("SaveImage")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    override fun toString(): String = route
}
