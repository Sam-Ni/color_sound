package com.example.colorsound.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorsound.*
import com.example.colorsound.data.DataSource
import com.example.colorsound.ui.theme.ColorSoundTheme
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

    ColorSoundTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination  = currentBackStack?.destination

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
            floatingActionButton = {
                ColorSoundFAB(
                    /* TODO */
                    onClick = { soundState++ }
                )
            }
        ) { paddingValues ->
            ColorSoundHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
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
