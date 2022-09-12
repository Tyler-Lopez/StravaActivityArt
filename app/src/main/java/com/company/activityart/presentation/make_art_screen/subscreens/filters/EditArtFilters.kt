package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent
import com.company.activityart.presentation.make_art_screen.EditArtViewEvent
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewState.*

@Composable
fun EditArtFilters(
    eventReceiver: ViewEventListener<EditArtViewEvent>,
    viewModel: EditArtViewModel = hiltViewModel()
) {
    viewModel.viewState.collectAsState().value?.apply {
        when (this) {
            is LoadingFilters -> CircularProgressIndicator()
            is Standby -> EditArtStandby(
                yearMonthEarliest = yearMonthEarliest,
                yearMonthLatest = yearMonthLatest,
                distanceMax = distanceMax,
                distanceMin = distanceMin,
                selectedActivitiesCount = selectedActivitiesCount
            )
        }
    }
}