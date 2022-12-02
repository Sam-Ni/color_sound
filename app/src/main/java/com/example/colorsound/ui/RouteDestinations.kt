package com.example.colorsound.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.colorsound.R


interface RouteDestination {
    val selectIconResId: Int
    val iconResId: Int
    val route: String
}

object Home : RouteDestination {
    override val selectIconResId: Int
        get() = R.drawable.home_select
    override val iconResId: Int
        get() = R.drawable.home
    override val route: String
        get() = "Home"
}

object World : RouteDestination {
    override val selectIconResId: Int
        get() = R.drawable.web_select
    override val iconResId: Int
        get() = R.drawable.web
    override val route: String
        get() = "World"
}

object Setting : RouteDestination {
    override val selectIconResId: Int
        get() = R.drawable.setting_select
    override val iconResId: Int
        get() = R.drawable.setting
    override val route: String
        get() = "Settings"
}

val colorSoundTabRowScreens = listOf(Home, World, Setting)










