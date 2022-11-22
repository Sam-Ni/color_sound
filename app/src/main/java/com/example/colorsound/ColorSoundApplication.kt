package com.example.colorsound

import android.app.Application
import com.example.colorsound.data.AppContainer
import com.example.colorsound.data.DefaultAppContainer

class ColorSoundApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}