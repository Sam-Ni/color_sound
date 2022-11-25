package com.example.colorsound.util

import androidx.compose.ui.graphics.Color

fun IndexToColor(index: Int): Color {
    return when (index) {
        0 -> Color(0xFFed353c)
        1 -> Color(0xFFf79f38)
        2 -> Color(0xFFfff151)
        3 -> Color(0xFF93c650)
        4 -> Color(0xFF11924f)
        5 -> Color(0xFF167abc)
        6 -> Color(0xFF6b3991)
        else -> Color(0xFFFFFFFF)
    }
}

const val COLOR_NUMBER = 7

const val BASE_URL = "http://192.168.3.10:8080/"