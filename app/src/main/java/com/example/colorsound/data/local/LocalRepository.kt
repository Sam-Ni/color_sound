package com.example.colorsound.data.local

import com.example.colorsound.model.Sound

interface LocalRepository {
    suspend fun getAllSounds(): List<Sound>

    suspend fun insertSound(sound: Sound)
}