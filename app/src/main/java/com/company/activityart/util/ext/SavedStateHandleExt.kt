package com.company.activityart.util.ext

import androidx.lifecycle.SavedStateHandle
import com.company.activityart.util.accessTokenNavSpec
import com.company.activityart.util.athleteIdNavSpec

val SavedStateHandle.athleteId: Long
    get() = get<Long>(athleteIdNavSpec.key) ?: error("AthleteID missing from SSH.")

val SavedStateHandle.accessToken: String
    get() = get<String>(accessTokenNavSpec.key) ?: error("Access token missing from SSH.")

