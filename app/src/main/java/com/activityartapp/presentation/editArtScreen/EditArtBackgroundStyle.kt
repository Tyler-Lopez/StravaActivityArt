package com.activityartapp.presentation.editArtScreen

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed interface EditArtBackgroundStyle : Parcelable {

    companion object {
        fun values() = EditArtBackgroundStyle::class.sealedSubclasses
    }

    @Parcelize
    object Transparent : EditArtBackgroundStyle

    @Parcelize
    data class Solid(val color: ColorWrapper) : EditArtBackgroundStyle
}