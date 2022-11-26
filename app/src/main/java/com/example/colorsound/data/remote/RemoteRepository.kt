package com.example.colorsound.data.remote

import com.example.colorsound.model.Sound

interface RemoteRepository {
    suspend fun getRandomSounds(): List<Sound>

    suspend fun uploadSound(sound: Sound)
}