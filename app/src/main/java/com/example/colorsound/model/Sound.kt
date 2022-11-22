package com.example.colorsound.model

import androidx.annotation.DrawableRes

data class Sound(
    @DrawableRes val color: Int,
    val name: String,
    val duration: String,
    val createTime: String,
    val url: String,
)
