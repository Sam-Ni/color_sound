package com.example.colorsound.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colorsound.model.Sound
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundDao {
    @Query("SELECT * FROM sound ORDER BY createTime DESC")
    fun getSounds(): List<Sound>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sound: Sound)

    @Delete
    suspend fun delete(sound: Sound)
}