package com.example.colorsound.data

import android.util.Log
import com.example.colorsound.model.Sound
import com.example.colorsound.network.ColorApiService


interface ColorSoundRepository {
    suspend fun getSounds(): List<Sound>
}

class NetworkColorSoundRepository(
    private val colorApiService: ColorApiService
) : ColorSoundRepository {
    override suspend fun getSounds(): List<Sound> {
        val responseSounds =  colorApiService.getSounds()
        return if (responseSounds.status == "SUCCESS") {
            responseSounds.data
        } else {
            emptyList()
        }
    }
}