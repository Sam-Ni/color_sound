package com.example.colorsound.network

import com.example.colorsound.model.ResponseSounds
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ColorApiService {
    @POST("sound/getRandom")
    suspend fun getSounds(): ResponseSounds

    @Multipart
    @POST("sound/upload")
    suspend fun uploadSound(
        @Part sound: MultipartBody.Part,
        @Part ("color") color: Int,
        @Part("duration") duration: RequestBody,
        @Part("name") name: RequestBody,
    ): ResponseBody

    @Multipart
    @POST("sound/getRandomByColor")
    suspend fun getColorSound(@Part ("color") color: Int): ResponseSounds
}