package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.FilterStateWrapper

@Composable
fun EditArtFilters(viewModel: EditArtFiltersViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        when (this) {
            is Loading -> CircularProgressIndicator()
            is Standby -> FilterSectionDate(
                dateRangeSecondsSelected,
                dateRangeSecondsSelectedYMDStart,
                dateRangeSecondsSelectedYMDEnd,
                dateRangeSecondsTotal,
                dateRangeYearsSelected,
                dateRangeYearsTotal,
                viewModel
            )
        }
    }
}