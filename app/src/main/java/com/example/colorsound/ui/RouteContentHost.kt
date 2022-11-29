package com.example.colorsound.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colorsound.ui.screens.home.HomeScreen
import com.example.colorsound.ui.screens.home.HomeScreenVM
import com.example.colorsound.ui.screens.settings.SettingsScreen
import com.example.colorsound.ui.screens.settings.SettingsScreenVM
import com.example.colorsound.ui.screens.splash.SplashScreen
import com.example.colorsound.ui.screens.world.WorldScreen
import com.example.colorsound.ui.screens.world.WorldScreenVM


@Composable
fun RouteContentHost(
    colorSoundHostVM: RouteContentHostVM,
    modifier: Modifier = Modifier,
) {
    colorSoundHostVM.apply {
        NavHost(
            navController = navController,
            startDestination = Home.route,
            modifier = modifier
        ) {
            composable(route = Home.route) {
                HomeScreen(homeScreenVM)
            }
            composable(route = World.route) {
                WorldScreen(worldScreenVM)
            }
            composable(route = Setting.route) {
                SettingsScreen(settingsScreenVM)
            }
        }
    }
}

data class RouteContentHostVM(
    val navController: NavHostController,
    val homeScreenVM: HomeScreenVM,
    val worldScreenVM: WorldScreenVM,
    val settingsScreenVM: SettingsScreenVM,
)

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
