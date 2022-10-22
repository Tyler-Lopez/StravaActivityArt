package com.company.activityart.presentation

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.presentation.MainViewState.*
import com.company.activityart.presentation.MainViewEvent.*
import com.company.activityart.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : BaseRoutingViewModel<
        MainViewState,
        MainViewEvent,
        MainDestination>() {

    init {
        pushState(LoadingAuthentication)
    }

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
                when (val response = getAccessTokenUseCase(event.uri)) {
                    is Success -> {
                        Authenticated(
                            athleteId = response.data.athleteId,
                            accessToken = response.data.accessToken
                        )
                    }
                    is Error -> Unauthenticated
                }
            )
        }
    }
}