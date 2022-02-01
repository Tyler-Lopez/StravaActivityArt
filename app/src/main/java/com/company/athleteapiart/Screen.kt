package com.company.athleteapiart

sealed class Screen(
    val route: String
) {
    object TimeSelect : Screen("TimeSelect")
    object FilterActivities : Screen("FilterActivities")
    object FormatActivitiesOne : Screen("FormatActivitiesOne")
    object FormatActivitiesTwo : Screen("FormatActivitiesTwo")
    object VisualizeActivities : Screen("VisualizeActivities")

    override fun toString(): String = route
}
