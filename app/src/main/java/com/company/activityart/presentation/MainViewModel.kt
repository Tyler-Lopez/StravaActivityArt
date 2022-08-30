package com.company.activityart.presentation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.architecture.Router
import com.company.activityart.architecture.StateSender
import com.company.activityart.presentation.MainViewState.*
import javax.inject.Inject

class MainViewModel : ViewModel(),
    EventReceiver<MainViewEvent>,
    StateSender<MainViewState> {

    // ViewState - observed in the view
    private var _viewState: MutableState<MainViewState> = mutableStateOf(LoadingAuthentication)
    override val viewState: State<MainViewState> = _viewState

    override fun onEvent(event: MainViewEvent) {
        when (event) {
            is MainViewEvent.LoadAuthentication -> {
            }
        }
    }

    private fun onParseIntent(uri: Uri?) {

    }

}