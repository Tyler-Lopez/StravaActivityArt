package com.activityartapp.presentation.errorScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.errorScreen.ErrorViewEvent.*
import com.activityartapp.util.NavArgSpecification.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(
    ssh: SavedStateHandle
) : BaseRoutingViewModel<ErrorViewState, ErrorViewEvent, MainDestination>() {

    private val errorScreenType = ErrorScreenType.valueOf(ErrorScreen.rawArg(ssh))

    init {
        ErrorViewState(error = errorScreenType).push()
    }
    override fun onEvent(event: ErrorViewEvent) {
        when (event) {
            is ClickedReturn -> onClickedReturn()
        }
    }

    private fun onClickedReturn() {
        viewModelScope.launch {
            routeTo(MainDestination.NavigateUp)
        }
    }
}