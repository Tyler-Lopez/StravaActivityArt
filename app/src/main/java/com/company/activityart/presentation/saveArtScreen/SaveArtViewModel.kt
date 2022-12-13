package com.company.activityart.presentation.saveArtScreen

import androidx.lifecycle.SavedStateHandle
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    init {
      //  val b: String? = savedStateHandle[NavArg.Bitmap.key]!!
       // val bitmap = Gson().fromJson(b, Bitmap::class.java)
       // println("yo here bitmap is $bitmap")
    }


    override fun onEvent(event: SaveArtViewEvent) {
        TODO("Not yet implemented")
    }
}