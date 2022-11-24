package com.example.colorsound.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorsound.*
import com.example.colorsound.data.DataSource
import com.example.colorsound.ui.components.ColorSoundTapRow
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.SaveDialog
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory

@Composable
fun ColorSoundApp() {

    val appViewModel: AppViewModel = viewModel()

    ColorSoundTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val currentScreen = colorSoundTabRowScreens.find { it.route == currentDestination?.route } ?: Home
        Scaffold(
            bottomBar = {
                ColorSoundTapRow(
                    allScreen = colorSoundTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                ) },
        ) { paddingValues ->
            Column {
                ColorSoundHost(
                    navController = navController,
                    appViewModel = appViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ColorSoundTheme {
        ColorSoundApp()
    }
}
