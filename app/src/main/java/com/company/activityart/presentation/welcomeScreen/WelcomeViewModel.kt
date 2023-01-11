package com.company.activityart.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.welcomeScreen.WelcomeViewEvent.*
import com.company.activityart.util.doOnError
import com.company.activityart.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val getAthleteUseCase: GetAthleteUseCase,
) : BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    private lateinit var athleteId: String
    private lateinit var accessToken: String

    init {
        loadAthlete()
    }

    override fun onEvent(event: WelcomeViewEvent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (event) {
                is ClickedAbout -> onClickedAbout()
                is ClickedMakeArt -> onClickedMakeArt()
                is ClickedLogout -> onClickedLogout()
            }
        }
    }

    private suspend fun onClickedAbout() {
        routeTo(NavigateAbout)
    }

    private suspend fun onClickedMakeArt() {
        /** Either route to screen where activities are loaded into RAM cache
         * or directly to Make Art if RAM cache is already present. */
        routeTo(
            if (getActivitiesFromCacheUseCase().isEmpty()) {
                NavigateLoadActivities(athleteId, accessToken)
            } else {
                NavigateEditArt(fromLoad = false)
            }
        )
    }

    private suspend fun onClickedLogout() {
        clearAccessTokenUseCase()
        routeTo(NavigateLogin)
    }

    private fun loadAthlete() {
        viewModelScope.launch(Dispatchers.IO) {
            getAccessTokenUseCase()
                .doOnSuccess {
                    athleteId = data.athleteId.toString()
                    accessToken = data.accessToken
                    getAthleteUseCase(data.athleteId, accessToken)
                        .doOnSuccess {
                            WelcomeViewState(
                                athleteName = data.fullName,
                                athleteImageUrl = data.profilePictureLarge
                            ).push()
                        }
                        .doOnError { routeTo(NavigateLogin) } // Todo, load error state instead
                }
                .doOnError {
                    clearAccessTokenUseCase()
                    routeTo(NavigateLogin)
                }
        }
    }
}