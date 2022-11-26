package com.example.colorsound.data.remote.impl

import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.model.Sound
import com.example.colorsound.network.ColorApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NetworkRepository(
    private val colorApiService: ColorApiService
) : RemoteRepository{
    override suspend fun getRandomSounds(): List<Sound> {
        val responseSounds =  colorApiService.getSounds()
        return if (responseSounds.status == "SUCCESS") {
            responseSounds.data
        } else { /* TODO handle failure */
            emptyList()
        }
    }

    override suspend fun uploadSound(sound: Sound) {
        val file = File(sound.url)
        val requestFile = file.asRequestBody("multipart/audio/mpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("sound", file.name, requestFile)
        val name = sound.name.toRequestBody("text/plain".toMediaType())
        val duration = sound.duration.toRequestBody("text/plain".toMediaType())
        val responseUpload = colorApiService.uploadSound(body, sound.color, duration, name)
    }
}