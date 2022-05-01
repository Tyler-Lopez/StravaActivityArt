package com.company.athleteapiart

sealed class Screen(
    val route: String
) {
    object TimeSelect : Screen("TimeSelect")
    object Login : Screen("Login")
    object FilterActivities : Screen("FilterActivities")
    object FormatActivitiesOne : Screen("FormatActivitiesOne")
    object FormatActivitiesTwo : Screen("FormatActivitiesTwo")
    object FormatActivitiesThree : Screen("FormatActivitiesThree")
    object FormatActivitiesFour : Screen("FormatActivitiesFour")
    object ErrorNoActivities : Screen("ErrorNoActivities")
    object VisualizeActivities : Screen("VisualizeActivities")
    object SaveImage : Screen("SaveImage")

    override fun toString(): String = route
}
