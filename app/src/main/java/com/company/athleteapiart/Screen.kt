package com.company.athleteapiart

sealed class Screen(
    val route: String
) {
    object TimeSelect : Screen("TimeSelect")
    object FilterActivities : Screen("FilterActivities")
    object FormatActivitiesOne : Screen("FormatActivitiesOne")
    object FormatActivitiesTwo : Screen("FormatActivitiesTwo")
    object FormatActivitiesThree : Screen("FormatActivitiesThree")
    object FormatActivitiesFour : Screen("FormatActivitiesFour")
    object VisualizeActivities : Screen("VisualizeActivities")
    object SaveImage : Screen("SaveImage")

    override fun toString(): String = route
}
