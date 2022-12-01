package com.example.colorsound

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.data.local.impl.DatabaseRepository
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.data.remote.impl.NetworkRepository
import com.example.colorsound.database.ColorSoundDatabase
import com.example.colorsound.network.ColorApiService
import com.example.colorsound.ui.vm.data.*
import com.example.colorsound.ui.vm.service.*
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
class ColorSoundApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ColorSoundDatabase.getDatabase(this, applicationScope) }
    private val databaseRepository by lazy { DatabaseRepository(database.soundDao()) }

    @ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build())
        .build()

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
                    isRepeatPlay = sharedPreferences.getBoolean(ConfigName.isRepeatPlay, false),
                    fileDir = applicationContext.filesDir.path
                )
            )
        }
        val onPushResultData = MutableStateFlow(OnPushResultData())
        val contextData = ContextData(applicationContext)

        Injecter.add<LocalRepository>(databaseRepository)
        Injecter.add(networkRepository)
        Injecter.add(localSoundListData)
        Injecter.add(saveSoundDialogData)
        Injecter.add(searchBarData)
        Injecter.add(recordData)
        Injecter.add(worldData)
        Injecter.add(maskData)
        Injecter.add(playSoundData)
        Injecter.add(worldColorData)
        Injecter.add<SharedPreferences>(getSharedPreferences("data", Context.MODE_PRIVATE))
        Injecter.add(highlightData)
        Injecter.add(configData)
        Injecter.add(onPushResultData)
        Injecter.add(contextData)


        val recordService = RecordService()
        val localSoundListService = LocalSoundListService()
        val playSoundService = PlaySoundService()
        val worldService = WorldService()
        val upLoadSoundService = UpLoadSoundService()
        val settingService = SettingService()
        val remoteSoundListService = RemoteSoundListService()


        Injecter.addService(recordService)
        Injecter.addService(localSoundListService)
        Injecter.addService(playSoundService)
        Injecter.addService(worldService)
        Injecter.addService(upLoadSoundService)
        Injecter.addService(settingService)
        Injecter.addService(remoteSoundListService)
    }
}