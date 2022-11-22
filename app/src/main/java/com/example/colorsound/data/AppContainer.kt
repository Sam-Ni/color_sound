package com.example.colorsound.data

import com.example.colorsound.network.ColorApiService
import com.example.colorsound.util.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

interface AppContainer {
    val colorSoundRepository: ColorSoundRepository
}

class DefaultAppContainer : AppContainer {
//    private val BASE_URL = "http://43.139.148.247:8080"


    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: ColorApiService by lazy {
        retrofit.create(ColorApiService::class.java)
    }

    override val colorSoundRepository: ColorSoundRepository by lazy {
        NetworkColorSoundRepository(retrofitService)
    }
}