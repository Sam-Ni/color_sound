package com.example.colorsound.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sound")
@kotlinx.serialization.Serializable
data class Sound(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val color: Int,
    val name: String,
    val createTime: String,
    val url: String,
)
