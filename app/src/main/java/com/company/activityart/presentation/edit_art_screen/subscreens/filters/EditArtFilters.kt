package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.FilterStateWrapper

@Composable
fun EditArtFilters(
    filterStateWrapper: FilterStateWrapper,
    parentEventReceiver: EventReceiver<EditArtViewEvent>,
    viewModel: EditArtFiltersViewModel = hiltViewModel()
) {
    filterStateWrapper.apply {
        val selectedRange = unixSecondSelectedStart..unixSecondSelectedEnd
        val totalRange = unixSecondTotalStart..unixSecondTotalEnd
        FilterSectionDate(
            selectedRange,
            totalRange,
            parentEventReceiver
        )
    }
}