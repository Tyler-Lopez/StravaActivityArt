package com.company.athleteapiart.presentation.format_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormatScreenTwoViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {
    var useConditionalFormatting = mutableStateOf(false)
}