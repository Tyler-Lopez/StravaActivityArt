package com.company.activityart

import android.content.Intent
import com.company.activityart.architecture.ViewEvent

sealed class MainViewEvent : ViewEvent {
    data class IntentReceived(val intent: Intent) : MainViewEvent()
}