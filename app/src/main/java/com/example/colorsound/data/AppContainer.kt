package com.example.colorsound.data

import android.content.SharedPreferences
import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.data.remote.impl.NetworkRepository
import com.example.colorsound.network.ColorApiService
import com.example.colorsound.ui.vm.data.*
import com.example.colorsound.util.BASE_URL
import com.example.colorsound.util.ConfigName
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val networkRepository: RemoteRepository
    val databaseRepository: LocalRepository
    val sharedPreferences: SharedPreferences
}

interface IDataContainer {
    val saveSoundDialogData: MutableStateFlow<SaveSoundDialogData>
    val localSoundListData: MutableStateFlow<LocalSoundListData>
    val searchBarData: MutableStateFlow<SearchBarData>
    val recordData: MutableStateFlow<RecordData>
    val worldData: MutableStateFlow<WorldData>
    val maskData: MutableStateFlow<MaskData>
    val playSoundData: MutableStateFlow<PlaySoundData>
    val worldColorData: MutableStateFlow<WorldColorData>
    val configData: MutableStateFlow<ConfigData>
}

class DefaultAppContainer(
    override val databaseRepository: LocalRepository,
    override val saveSoundDialogData: MutableStateFlow<SaveSoundDialogData>,
    override val localSoundListData: MutableStateFlow<LocalSoundListData>,
    override val searchBarData: MutableStateFlow<SearchBarData>,
    override val recordData: MutableStateFlow<RecordData>,
    override val worldData: MutableStateFlow<WorldData>,
    override val maskData: MutableStateFlow<MaskData>,
    override val playSoundData: MutableStateFlow<PlaySoundData>,
    override val worldColorData: MutableStateFlow<WorldColorData>,
    override val sharedPreferences: SharedPreferences,

    ) : AppContainer, IDataContainer {
    override val configData: MutableStateFlow<ConfigData> by lazy {
        if (!sharedPreferences.contains(ConfigName.isRepeatPlay)) {
            sharedPreferences.edit().apply {
                putBoolean(ConfigName.isRepeatPlay, false)
                commit()
            }
        }

        MutableStateFlow(
            ConfigData(
                isRepeatPlay = sharedPreferences.getBoolean(ConfigName.isRepeatPlay, false)
            )
        )
    }

    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL).build()

    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofitService: ColorApiService by lazy {
        retrofit.create(ColorApiService::class.java)
    }

    override val networkRepository: RemoteRepository by lazy {
        NetworkRepository(retrofitService)
    }
}