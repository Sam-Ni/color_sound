package com.example.colorsound.network

import com.example.colorsound.model.ResponseSounds
import com.example.colorsound.model.Sound
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST

interface ColorApiService {
    @POST("sound/getRandom")
    suspend fun getSounds(): ResponseSounds
}