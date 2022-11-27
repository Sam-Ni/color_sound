package com.example.colorsound

import android.app.Application
import android.content.Context
import com.example.colorsound.data.DefaultAppContainer
import com.example.colorsound.data.local.impl.DatabaseRepository
import com.example.colorsound.database.ColorSoundDatabase
import com.example.colorsound.ui.vm.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

class ColorSoundApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ColorSoundDatabase.getDatabase(this, applicationScope) }
    private val databaseRepository by lazy { DatabaseRepository(database.soundDao()) }
    lateinit var container: DefaultAppContainer
    lateinit var filesDir: String

    override fun onCreate() {
        super.onCreate()
        filesDir = applicationContext.filesDir.path
        container = DefaultAppContainer(
            databaseRepository,
            MutableStateFlow(SaveSoundDialogData()),
            MutableStateFlow(LocalSoundListData()),
            MutableStateFlow(SearchBarData()),
            MutableStateFlow(RecordData()),
            MutableStateFlow(WorldData()),
            MutableStateFlow(MaskData()),
            MutableStateFlow(PlaySoundData()),
            MutableStateFlow(WorldColorData()),
            getSharedPreferences("data", Context.MODE_PRIVATE)
        )
    }
}