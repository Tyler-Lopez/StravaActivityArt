package com.company.activityart.presentation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.architecture.ViewStateSender
import com.company.activityart.presentation.MainViewState.*
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent
import com.company.activityart.presentation.login_screen.LoginScreenViewState

class MainViewModel : BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {

    init {
        pushState(LoadingAuthentication)
    }

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is MainViewEvent.LoadAuthentication -> {
            }
        }
    }

    private fun onParseIntent(uri: Uri?) {

    }

}