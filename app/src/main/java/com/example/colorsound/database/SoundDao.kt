package com.example.colorsound.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colorsound.model.Sound
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundDao {
    @Query("SELECT * FROM sound ORDER BY createTime ASC")
    fun getSounds(): List<Sound>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sound: Sound)
}