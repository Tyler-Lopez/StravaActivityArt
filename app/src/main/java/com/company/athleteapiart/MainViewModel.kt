package com.company.athleteapiart

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.util.OAuth2Legacy

class MainViewModel(uri: Uri? = null) : ViewModel() {

    init {
        // If received, parse out authorizationCode (distinct from access token)
        if (uri != null) {
            OAuth2Legacy.authorizationCode = uri
                .toString()
                .substring(43)
                .substringBefore('&')
        }
    }
}