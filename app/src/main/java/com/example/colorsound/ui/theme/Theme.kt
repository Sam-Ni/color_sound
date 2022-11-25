package com.example.colorsound.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    background = Black,
    surface = DarkGrey,
    onSurface = White,
//    primary = Grey50,
//    onPrimary = Grey900,
//    secondary = Grey700
)

private val LightColorPalette = lightColorScheme(
    background = Grey,
    surface = White,
    onSurface = LightBlack,
//    primary = White,
//    onPrimary = LightBlack,
//    secondary = Grey
)

@Composable
fun ColorSoundTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}