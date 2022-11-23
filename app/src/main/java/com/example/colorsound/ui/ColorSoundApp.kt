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
import com.example.colorsound.util.AskPermission
import com.example.colorsound.util.SoundInfoFactory

@Composable
fun ColorSoundApp() {

    var soundState by remember {
        mutableStateOf(0)
    }

    val soundList = remember {
        mutableStateListOf(*DataSource.soundList.toTypedArray())
    }

    if (soundState == 2) {
        /* TODO  */
        SaveDialog(
            onCancelClick = { soundState = 0 },
            onSaveClick = {
                soundState = 0
                soundList.add(0, SoundInfoFactory())
            }
        )
    }

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
                AskPermission()
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
