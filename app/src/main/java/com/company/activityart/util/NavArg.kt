package com.company.activityart.util

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.company.activityart.domain.models.Activity
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

sealed class NavArg(val navArg: NamedNavArgument) {

    companion object {
        private const val ATHLETE_ID_KEY = "athleteId"
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val BITMAP_KEY = "bitmap"
    }

    object AthleteId : NavArg(navArgument(name = ATHLETE_ID_KEY) {
        type = NavType.LongType
    })
    object AccessToken : NavArg(navArgument(name = ACCESS_TOKEN_KEY) {
        type = NavType.StringType
    })

    object Bitmap : NavArg(navArgument(name = BITMAP_KEY) {
        type = NavType.StringType
    })

    val key: String = navArg.name
    val route: String = "$key={$key}"
}
