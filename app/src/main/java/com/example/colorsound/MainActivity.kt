package com.example.colorsound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.colorsound.ui.ColorSoundApp
import com.example.colorsound.ui.theme.ColorSoundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorSoundTheme {
                ColorSoundApp()
            }
        }
    }
}

