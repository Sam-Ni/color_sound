package com.example.colorsound

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.HomeScreen
import com.example.colorsound.ui.screens.home.HomeScreenVM
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
            val coroutineScope = rememberCoroutineScope()
            val homeUiState by homeViewModel.uiState.collectAsState()
            HomeScreen(
                HomeScreenVM(
                    onPlayOrPause = appViewModel::play,
                    onCardLongClick = { homeViewModel.onCardLongClick(it) },
                    onSaveClick = {
                        homeViewModel.onSaveClick()
                        homeViewModel.scrollToTop(coroutineScope)
                    },
                    onCancelClick = homeViewModel::onCancelClick,
                    onNameChanged = homeViewModel::updateSaveName,
                    chooseColor = homeViewModel::updateChoice,
                    onSearchValueChanged = homeViewModel::updateSearch,
                    search = homeUiState.search,
                    listState = homeUiState.listState,
                    soundList = homeUiState.soundList,
                    highlightSound = homeUiState.highlightSound,
                    showSaveDialog = homeUiState.showSaveDialog,
                    saveName = homeUiState.saveName,
                    color = homeUiState.color,
                )
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
