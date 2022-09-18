package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.Loading
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.Standby

@Composable
fun EditArtFiltersViewDelegate(viewModel: EditArtFiltersViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        when (this) {
            is Loading -> CircularProgressIndicator()
            is Standby -> EditArtFiltersStandby(
                dateMaxDateUnixMilliseconds = dateMaxDateUnixMilliseconds,
                dateMinDateUnixMilliSeconds = dateMinDateUnixMilliSeconds,
                dateYearMonthDayAfter = dateYearMonthDayAfter,
                dateYearMonthDayBefore = dateYearMonthDayBefore,
                typesWithSelectedFlag = typesWithSelectedFlag,
                eventReceiver = viewModel
            )
        }
    }
}