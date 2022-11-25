package com.example.colorsound

import android.app.Application
import com.example.colorsound.data.AppContainer
import com.example.colorsound.data.DefaultAppContainer
import com.example.colorsound.data.local.impl.DatabaseRepository
import com.example.colorsound.database.ColorSoundDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ColorSoundApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ColorSoundDatabase.getDatabase(this, applicationScope) }
    private val databaseRepository by lazy { DatabaseRepository(database.soundDao()) }
    lateinit var container: AppContainer
    lateinit var filesDir: String
    override fun onCreate() {
        super.onCreate()
        filesDir = applicationContext.filesDir.path
        container = DefaultAppContainer(databaseRepository)
    }
}