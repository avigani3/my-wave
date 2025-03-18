package it.unimib.mywave.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.unimib.mywave.view.screens.home.InitialScreen
import it.unimib.mywave.ui.theme.AppTheme
import it.unimib.mywave.utils.ServiceLocator


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ServiceLocator.init(this)
        setContent {
            AppTheme {
                // Determine if the app is offline
                InitialScreen()
            }
        }
    }
}


