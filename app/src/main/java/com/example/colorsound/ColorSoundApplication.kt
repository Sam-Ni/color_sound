package com.example.colorsound

import android.app.Application
import com.example.colorsound.data.AppContainer
import com.example.colorsound.data.DefaultAppContainer
import java.io.File

class ColorSoundApplication : Application() {
    lateinit var container: AppContainer
    lateinit var filesDir: String
    override fun onCreate() {
        super.onCreate()
        filesDir = applicationContext.filesDir.path
        container = DefaultAppContainer()
    }
}