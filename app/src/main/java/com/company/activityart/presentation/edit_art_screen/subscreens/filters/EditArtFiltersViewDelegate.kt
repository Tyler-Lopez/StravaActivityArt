package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*

@Composable
fun EditArtFiltersViewDelegate(viewModel: EditArtFiltersViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        when (this) {
            is Loading -> {}
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