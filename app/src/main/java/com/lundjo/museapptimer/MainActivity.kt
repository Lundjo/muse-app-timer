package com.lundjo.museapptimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lundjo.museapptimer.navigation.AppNavigation
import com.lundjo.museapptimer.ui.theme.MuseAppTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuseAppTimerTheme {
                AppNavigation()
            }
        }
    }
}