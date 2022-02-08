package com.company.athleteapiart.presentation.save_image_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.presentation.activity_visualize_screen.activitiesVisualizeCanvas
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Constants
import com.company.athleteapiart.util.saveImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveImageViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activities = AthleteActivities.filteredActivities
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var imageSavedAt = mutableStateOf("")

    fun startSave(context: Context) {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000)
            saveImage(
                bitmap = activitiesVisualizeCanvas(
                    maxWidth = 3420,
                    activities = activities.value
                ),
                context = context,
                folderName = Constants.IMAGE_DIRECTORY
            )
            isLoading.value = false
            endReached.value = true
        }
    }
}