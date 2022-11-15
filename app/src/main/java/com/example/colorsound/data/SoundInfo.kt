package com.example.colorsound.data

import androidx.annotation.DrawableRes
import com.example.colorsound.R

data class SoundInfo(
    @DrawableRes val color: Int,
    val name: String,
    val duration: String,
    val createTime: String,
)
