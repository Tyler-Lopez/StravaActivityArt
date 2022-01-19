package com.company.athleteapiart

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.util.Oauth2

class MainViewModel(uri: Uri? = null) : ViewModel() {

    init {
        // If received, parse out authorizationCode (distinct from access token)
        if (uri != null) {
            Oauth2.authorizationCode = uri
                .toString()
                .substring(43)
                .substringBefore('&')
        }
    }
}