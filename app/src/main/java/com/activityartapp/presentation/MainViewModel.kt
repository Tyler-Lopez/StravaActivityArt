package com.activityartapp.presentation

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.useCase.authentication.GetAthleteFromDiskOrRemote
import com.activityartapp.presentation.MainViewEvent.LoadAuthentication
import com.activityartapp.presentation.MainViewState.Authenticated
import com.activityartapp.presentation.MainViewState.Unauthenticated
import com.activityartapp.util.Response.Error
import com.activityartapp.util.Response.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAthleteFromDiskOrRemote: GetAthleteFromDiskOrRemote
) : BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {


    var athleteId: Long? = null
    var accessToken: String? = null

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is LoadAuthentication -> onLoadAuthentication(event)
        }
    }

    private fun onLoadAuthentication(event: LoadAuthentication) {
        viewModelScope.launch {
            pushState(
                when (val response = getAthleteFromDiskOrRemote(event.uri)) {
                    is Success -> Authenticated
                    is Error -> if (response.data != null) {
                        Authenticated
                    } else {
                        Unauthenticated
                    }
                }
            )
        }
    }
}