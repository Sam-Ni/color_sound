package com.example.colorsound.ui

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorsound.ColorSoundHost
import com.example.colorsound.Home
import com.example.colorsound.colorSoundTabRowScreens
import com.example.colorsound.navigateSingleTopTo
import com.example.colorsound.ui.components.bottomBar.ColorSoundTapRow
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.HomeViewModel
import com.example.colorsound.ui.screens.world.WorldViewModel
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundApp() {

    val appViewModel: AppViewModel = viewModel()

    val appUiState by appViewModel.uiState.collectAsState()

    val worldViewModel: WorldViewModel =
        viewModel(factory = WorldViewModel.Factory)
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val homeUiState by homeViewModel.uiState.collectAsState()

    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val askPermission by lazy {
        {
            audioPermissionState.launchPermissionRequest()
        }
    }
    val isGranted = audioPermissionState.status.isGranted

    ColorSoundTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val currentScreen =
            colorSoundTabRowScreens.find { it.route == currentDestination?.route } ?: Home
        Scaffold(
            bottomBar = {
                ColorSoundTapRow(
                    allScreen = colorSoundTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen,
                    onClick = homeViewModel::onClick,
                    onLongClick = homeViewModel::onLongClick,
                    recordState = homeUiState.recordState,
                    isGranted = isGranted,
                    askPermission = askPermission,
                    isHighlightMode = appUiState.highlightMode,
                    onDelete = { appUiState.highlightSound?.let {
                        homeViewModel.onDelete(it)
                        appViewModel.exitHighlight()
                    } }
                )
            },
        ) { paddingValues ->
            Column {
                ColorSoundHost(
                    navController = navController,
                    appViewModel = appViewModel,
                    homeViewModel = homeViewModel,
                    worldViewModel = worldViewModel,
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
