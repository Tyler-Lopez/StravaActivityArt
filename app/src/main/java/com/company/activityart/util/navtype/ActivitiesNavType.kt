package com.company.activityart.util.navtype

import android.os.Bundle
import androidx.navigation.NavType
import com.company.activityart.domain.models.Activities
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


/**
 * https://developer.android.com/guide/navigation/navigation-kotlin-dsl#custom-types
 */
class ActivitiesNavType : NavType<Activities>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): Activities {
        return bundle.getParcelable<Activities>(key) as Activities
    }

    override fun parseValue(value: String): Activities {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Activities) {
        bundle.putParcelable(key, value)
    }
}
