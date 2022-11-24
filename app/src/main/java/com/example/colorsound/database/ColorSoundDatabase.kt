package com.example.colorsound.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.colorsound.model.Sound
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Sound::class], version = 1, exportSchema = false)
abstract class ColorSoundDatabase : RoomDatabase(){

    abstract fun soundDao(): SoundDao

    companion object {
        @Volatile
        private var INSTANCE: ColorSoundDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ColorSoundDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColorSoundDatabase::class.java,
                    "sound_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}