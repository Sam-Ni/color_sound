package com.example.colorsound

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import com.example.colorsound.ui.screens.AppUiState
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.HomeScreen
import com.example.colorsound.ui.screens.home.HomeViewModel
import com.example.colorsound.ui.screens.settings.SettingsScreen
import com.example.colorsound.ui.screens.world.WorldScreen
import com.example.colorsound.ui.screens.world.WorldViewModel


@Composable
fun ColorSoundHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    homeViewModel: HomeViewModel,
    worldViewModel: WorldViewModel,
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                onPlayOrPause = appViewModel::play,
                homeViewModel = homeViewModel,
                onCardLongClick = {
                    homeViewModel.onCardLongClick(it)
                                  },
            )
        }
        composable(route = World.route) {
            WorldScreen(
                worldUiState = worldViewModel.worldUiState,
                retryAction = worldViewModel::getRandomSounds,
                onPlayOrPause = appViewModel::play,
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
