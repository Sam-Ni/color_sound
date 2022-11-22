package com.example.colorsound.model

import androidx.annotation.DrawableRes

@kotlinx.serialization.Serializable
data class Sound(
    val id: Int,
    val color: Int,
    val name: String,
    val createTime: String,
    val url: String,
)
