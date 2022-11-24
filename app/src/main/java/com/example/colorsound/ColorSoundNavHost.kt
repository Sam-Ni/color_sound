package com.example.colorsound

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colorsound.data.DataSource.soundList
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.HomeScreen
import com.example.colorsound.ui.screens.SettingsScreen
import com.example.colorsound.ui.screens.WorldScreen
import com.example.colorsound.ui.screens.home.HomeViewModel
import com.example.colorsound.ui.screens.world.WorldViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
) {
    val worldViewModel: WorldViewModel =
        viewModel(factory = WorldViewModel.Factory)
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val askPermission by lazy { {
        audioPermissionState.launchPermissionRequest()
    }
    }
    val isGranted = audioPermissionState.status.isGranted

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                soundList = soundList,
                onClickStartPlay = appViewModel::play,
                homeViewModel = homeViewModel,
                isGranted = isGranted,
                askPermission = askPermission,
            )
        }
        composable(route = World.route) {
            WorldScreen(
                worldUiState = worldViewModel.worldUiState,
                retryAction = worldViewModel::getRandomSounds,
                onClickStartPlay = appViewModel::play,
            )
        }
        composable(route = Setting.route) {
            SettingsScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
