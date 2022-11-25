package com.example.colorsound.data

import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.data.remote.impl.NetworkRepository
import com.example.colorsound.network.ColorApiService
import com.example.colorsound.util.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val networkRepository: RemoteRepository
    val databaseRepository: LocalRepository
}

class DefaultAppContainer(
                          override val databaseRepository: LocalRepository
) : AppContainer {

    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofitService: ColorApiService by lazy {
        retrofit.create(ColorApiService::class.java)
    }

    override val networkRepository: RemoteRepository by lazy {
        NetworkRepository(retrofitService)
    }
}