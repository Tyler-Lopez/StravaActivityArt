package com.example.stravaart.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stravaart.model.Athlete
import com.example.stravaart.network.ApiService
import kotlinx.coroutines.launch

class AthleteViewModel : ViewModel() {
    var athleteResponse: Athlete? by mutableStateOf(null)
    var errorMessage: String by mutableStateOf("")

    fun getAthlete() {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val athlete = apiService.getAthlete()
                athleteResponse = athlete
            } catch (e: Exception) {
                errorMessage = "${e.message}"
            }

        }
    }

}