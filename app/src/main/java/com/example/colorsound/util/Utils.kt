package com.example.colorsound.util

import com.example.colorsound.R
import com.example.colorsound.model.Sound

fun SoundInfoFactory(): Sound {
    return Sound(R.drawable.circle, "newAdd", "0", "123", "1")
}
