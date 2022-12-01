package com.example.colorsound.util

import androidx.compose.ui.graphics.Color

fun indexToColor(index: Int): Color {
    return when (index) {
        0 -> Color(0xFFed353c)
        1 -> Color(0xFFf79f38)
        2 -> Color(0xFFF6E200)
        3 -> Color(0xFF93c650)
        4 -> Color(0xFF11924f)
        5 -> Color(0xFF167abc)
        6 -> Color(0xFF6b3991)
        else -> Color(0xFFFFFFFF)
    }
}

fun indexToBackColor(index: Int, isSystemInDarkTheme: Boolean): Color {
    if (!isSystemInDarkTheme) {
        return when (index) {
            0 -> Color(0xFFEFD7D8)
            1 -> Color(0xFFF1DFCA)
            2 -> Color(0xFFFAF7DB)
            3 -> Color(0xFFE5F0D8)
            4 -> Color(0xFFE1F6EB)
            5 -> Color(0xFFCBDFEC)
            6 -> Color(0xFFEFE3F8)
            else -> Color(0xFFFFFFFF)
        }
    } else {
        return when (index) {
            0 -> Color(0xFF7D1216)
            1 -> Color(0xFF9D5D12)
            2 -> Color(0xFF897F0C)
            3 -> Color(0xFF4C7D0D)
            4 -> Color(0xFF054022)
            5 -> Color(0xFF084872)
            6 -> Color(0xFF310850)
            else -> Color(0xFF000000)
        }

    }
}

const val COLOR_NUMBER = 7

const val BASE_URL = "http://43.139.148.247:8082/"