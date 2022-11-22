package com.example.colorsound

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colorsound.data.DataSource.soundList
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.HomeScreen
import com.example.colorsound.ui.screens.SettingsScreen
import com.example.colorsound.ui.screens.WorldScreen
import com.example.colorsound.ui.screens.world.WorldViewModel


@Composable
fun ColorSoundHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
) {
    val worldViewModel: WorldViewModel =
        viewModel(factory = WorldViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            appViewModel.enableFAB()
            HomeScreen(
                soundList = soundList,
                onClickStartPlay = appViewModel::play,
            )
        }
        composable(route = World.route) {
            appViewModel.disableFAB()
            WorldScreen(
                worldUiState = worldViewModel.worldUiState,
                retryAction = worldViewModel::getSounds,
                onClickStartPlay = appViewModel::play,
            )
        }
        composable(route = Setting.route) {
            appViewModel.disableFAB()
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
