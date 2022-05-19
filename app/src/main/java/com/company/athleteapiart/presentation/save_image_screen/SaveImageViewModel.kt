package com.company.athleteapiart.presentation.save_image_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@HiltViewModel
class SaveImageViewModel @Inject constructor(
) : ViewModel() {

    var activities = AthleteActivities.filteredActivities
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var imageSavedAt = mutableStateOf("")

    fun startSave(context: Context) {

        isLoading.value = true
        viewModelScope.launch {
            delay(2000)
            /*
            saveImage(
                bitmap = activitiesVisualizeCanvas(
                    maxWidth = 3420,
                    context = context,
                    activities = activities.value
                ),
                context = context,
                folderName = Constants.IMAGE_DIRECTORY
            )

             */
            isLoading.value = false
            endReached.value = true
        }


    }
}

 */