package com.example.stravaart

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.stravaart.ui.theme.StravaArtTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    // https://developer.android.com/training/id-auth/authenticate
        val am: AccountManager = AccountManager.get(this)
        val options = Bundle()

        val onTokenAcquired = AccountManagerCallback<Bundle> {
            TODO("Not yet implemented")
        }

        am.getAuthToken(
            Account("test", "test"),
           "read_all",
            options,
            this,
            onTokenAcquired,
            Handler()
        )

        super.onCreate(savedInstanceState)
        setContent {
            StravaArtTheme {

            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StravaArtTheme {
        Greeting("Android")
    }
}