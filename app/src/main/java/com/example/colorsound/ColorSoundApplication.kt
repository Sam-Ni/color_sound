package com.example.colorsound

import android.app.Application
import android.content.Context
import com.example.colorsound.data.local.impl.DatabaseRepository
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.data.remote.impl.NetworkRepository
import com.example.colorsound.database.ColorSoundDatabase
import com.example.colorsound.network.ColorApiService
import com.example.colorsound.ui.vm.data.*
import com.example.colorsound.util.BASE_URL
import com.example.colorsound.util.ConfigName
import com.example.colorsound.util.Injecter
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class ColorSoundApplication : Application() {
    private var injecter: Injecter = Injecter.instance()

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ColorSoundDatabase.getDatabase(this, applicationScope) }
    private val databaseRepository by lazy { DatabaseRepository(database.soundDao()) }

    @ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL).build()

    @ExperimentalSerializationApi
    private val retrofitService: ColorApiService by lazy {
        retrofit.create(ColorApiService::class.java)
    }

    private val networkRepository: RemoteRepository by lazy {
        NetworkRepository(retrofitService)
    }

    override fun onCreate() {
        super.onCreate()
        val localSoundListData = MutableStateFlow(LocalSoundListData())
        val saveSoundDialogData = MutableStateFlow(SaveSoundDialogData())
        val searchBarData = MutableStateFlow(SearchBarData())
        val recordData = MutableStateFlow(RecordData())
        val worldData = MutableStateFlow(WorldData())
        val maskData = MutableStateFlow(MaskData())
        val playSoundData = MutableStateFlow(PlaySoundData())
        val worldColorData = MutableStateFlow(WorldColorData())
        val highlightData = MutableStateFlow(HighlightData())
        val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val configData: MutableStateFlow<ConfigData> by lazy {
            MutableStateFlow(
                ConfigData(
                    isRepeatPlay = sharedPreferences.getBoolean(ConfigName.isRepeatPlay, false)
                )
            )
        }

        injecter.add("FilesDir", applicationContext.filesDir.path)
        injecter.add("DatabaseRepository", databaseRepository)
        injecter.add("NetworkRepository", networkRepository)
        injecter.add("LocalSoundListData", localSoundListData)
        injecter.add("SaveSoundDialogData", saveSoundDialogData)
        injecter.add("SearchBarData", searchBarData)
        injecter.add("RecordData", recordData)
        injecter.add("WorldData", worldData)
        injecter.add("MaskData", maskData)
        injecter.add("PlaySoundData", playSoundData)
        injecter.add("WorldColorData", worldColorData)
        injecter.add("SharedPreferences",  getSharedPreferences("data", Context.MODE_PRIVATE))
        injecter.add("HighlightData", highlightData)
        injecter.add("ConfigData", configData)
    }
}