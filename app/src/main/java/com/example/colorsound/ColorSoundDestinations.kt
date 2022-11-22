package com.example.colorsound

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

interface ColorSoundDestination {
    val icon: ImageVector
    val route: String
}

object Home : ColorSoundDestination {
    override val icon: ImageVector
        get() = Icons.Filled.Home
    override val route: String
        get() = "Home"
}

object World : ColorSoundDestination {
    override val icon: ImageVector
        get() = Icons.Filled.Person
    override val route: String
        get() = "World"
}

object Setting : ColorSoundDestination {
    override val icon: ImageVector
        get() = Icons.Filled.Settings
    override val route: String
        get() = "Settings"
}

val colorSoundTabRowScreens = listOf(Home, World, Setting)










