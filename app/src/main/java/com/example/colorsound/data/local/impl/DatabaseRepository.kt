package com.example.colorsound.data.local.impl

import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.database.SoundDao
import com.example.colorsound.model.Sound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class DatabaseRepository(
    private val soundDao: SoundDao
) : LocalRepository {

    override suspend fun getAllSounds(): List<Sound> {
        return withContext(Dispatchers.IO) {
            soundDao.getSounds()
        }
    }

    override suspend fun insertSound(sound: Sound) {
        withContext(Dispatchers.IO) {
            soundDao.insert(sound)
        }
    }

    override suspend fun deleteSound(sound: Sound) {
        withContext(Dispatchers.IO) {
            soundDao.delete(sound)
        }
    }

    override suspend fun updateSound(sound: Sound) {
        withContext(Dispatchers.IO) {
            soundDao.update(sound)
        }
    }
}