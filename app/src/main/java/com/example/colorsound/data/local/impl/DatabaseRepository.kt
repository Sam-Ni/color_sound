package com.example.colorsound.data.local.impl

import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.database.SoundDao
import com.example.colorsound.model.Sound

class DatabaseRepository(
    private val soundDao: SoundDao
) : LocalRepository {
    override suspend fun getAllSounds(): List<Sound> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSound(sound: Sound) {
        soundDao.insert(sound)
    }
}