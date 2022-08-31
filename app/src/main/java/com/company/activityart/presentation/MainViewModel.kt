package com.company.activityart.presentation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.architecture.ViewStateSender
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.presentation.MainViewState.*
import com.company.activityart.presentation.MainViewEvent.*
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent
import com.company.activityart.presentation.login_screen.LoginScreenViewState
import com.company.activityart.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
): BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {

    init {
        pushState(LoadingAuthentication)
    }

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is LoadAuthentication -> onLoadAuthentication(event)
        }
    }

    private fun onLoadAuthentication(event: LoadAuthentication) {
        viewModelScope.launch {
            pushState(when (getAccessTokenUseCase(event.uri)) {
                is Success -> Authenticated
                is Error -> Unauthenticated
            })
        }
    }

    private fun onParseIntent(uri: Uri?) {

    }

}