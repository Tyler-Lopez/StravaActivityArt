package com.company.activityart.util.ext

import androidx.lifecycle.SavedStateHandle
import com.company.activityart.util.NavArg
import com.company.activityart.util.NavArg.*

val SavedStateHandle.athleteId: Long
    get() = get<Long>(NavArg.athleteId.key) ?: error("AthleteID missing from SSH.")

val SavedStateHandle.accessToken: String
    get() = get<String>(NavArg.accessToken.key) ?: error("Access token missing from SSH.")

