package com.company.athleteapiart.presentation.athletescreen

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activity = mutableStateOf<List<ActivityDetailed>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        getActivity()
    }

    private fun getActivity() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getActivity()) {
                is Resource.Success -> {
                    activity.value = listOf(result.data)
                    isLoading.value = false
                }
                is Resource.Error -> {
                    println("ERROR FOUND")
                    println(result.message)
                    isLoading.value = false
                }
            }
        }
    }

    private var _onBitmapCreated = MutableLiveData<Bitmap?>(null)
    var onBitmapGenerated: LiveData<Bitmap?> = _onBitmapCreated

    fun bitmapCreated(bitmap: Bitmap?) {
        _onBitmapCreated.value = bitmap
    }
}