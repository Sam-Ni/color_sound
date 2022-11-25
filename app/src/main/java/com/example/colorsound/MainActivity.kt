package com.example.colorsound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import com.example.colorsound.ui.ColorSoundApp
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ColorSoundTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(
                    MaterialTheme.colorScheme.background,
                    darkIcons = !isSystemInDarkTheme()
                )
                ColorSoundApp()
            }
        }
    }
}

