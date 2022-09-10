package com.company.activityart.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activities(
    val activitiesByYear: Map<Int, List<ParcelableActivity>>
) : Parcelable