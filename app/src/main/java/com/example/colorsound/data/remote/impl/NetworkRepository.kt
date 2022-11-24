package com.example.colorsound.data.remote.impl

import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.model.Sound
import com.example.colorsound.network.ColorApiService

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
}