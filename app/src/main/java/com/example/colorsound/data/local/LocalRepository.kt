package com.example.colorsound.data.local

import com.example.colorsound.model.Sound
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getAllSounds(): List<Sound>

    suspend fun insertSound(sound: Sound)

    fun observeSounds(): Flow<List<Sound>>

    suspend fun deleteSound(sound: Sound)
}