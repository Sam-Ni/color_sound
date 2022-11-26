package com.example.colorsound.ui

import android.Manifest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorsound.ui.components.bottomBar.ScreenBar
import com.example.colorsound.ui.components.bottomBar.ScreenBarVM
import com.example.colorsound.ui.screens.AppViewModel
import com.example.colorsound.ui.screens.home.HomeScreenVM
import com.example.colorsound.ui.screens.home.HomeViewModel
import com.example.colorsound.ui.screens.world.WorldScreenVM
import com.example.colorsound.ui.screens.world.WorldViewModel
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundApp() {

    val appViewModel: AppViewModel = viewModel()
    val worldViewModel: WorldViewModel = viewModel(factory = WorldViewModel.Factory)
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

    val homeUiState by homeViewModel.uiState.collectAsState()
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val askPermission by lazy {
        {
            audioPermissionState.launchPermissionRequest()
        }
    }
    val isGranted = audioPermissionState.status.isGranted

    ColorSoundTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            MaterialTheme.colorScheme.background, darkIcons = !isSystemInDarkTheme()
        )

        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val currentScreen =
            colorSoundTabRowScreens.find { it.route == currentDestination?.route } ?: Home

        val screenBarVM = ScreenBarVM(allScreen = colorSoundTabRowScreens,
            onTabSelected = { newScreen ->
                navController.navigateSingleTopTo(newScreen.route)
            },
            currentScreen = currentScreen,
            onClick = { homeViewModel.onClick(appViewModel::updateMask) },
            onLongClick = { homeViewModel.onLongClick(appViewModel::updateMask) },
            recordState = homeUiState.recordState,
            isGranted = isGranted,
            askPermission = askPermission,
            isHighlightMode = homeUiState.highlightMode,
            onDelete = {
                homeViewModel.onDelete()
                appViewModel.updateMask(false)
            })
        val coroutineScope = rememberCoroutineScope()
        val routeContentHostVM = RouteContentHostVM(
            navController = navController,
            homeScreenVM = HomeScreenVM(
                onPlayOrPause = appViewModel::play,
                onCardLongClick = {
                    homeViewModel.onCardLongClick(it)
                    appViewModel.updateMask(true)
                },
                onSaveDialogSaveBtnClick = {
                    homeViewModel.onSaveClick()
                    homeViewModel.scrollToTop(coroutineScope)
                },
                onSaveDialogCancelBtnClick = homeViewModel::onCancelClick,
                onSaveDialogSoundNameChanged = homeViewModel::updateSaveName,
                saveDialogChooseSoundColor = homeViewModel::updateChoice,
                onSearchBarValueChanged = homeViewModel::updateSearch,
                searchBarText = homeUiState.search,
                soundListState = homeUiState.listState,
                soundList = homeUiState.soundList,
                highlightSound = homeUiState.highlightSound,
                isShowSaveDialog = homeUiState.showSaveDialog,
                saveDialogSoundNameText = homeUiState.saveName,
                saveDialogChosenColor = homeUiState.color,
            ), worldScreenVM = WorldScreenVM(
                worldUiState = worldViewModel.worldUiState,
                retryAction = worldViewModel::getRandomSounds,
                onPlayOrPause = appViewModel::play,
            )
        )

        Scaffold(
            bottomBar = {
                ScreenBar(screenBarVM)
            },
        ) { paddingValues ->
            Column {
                RouteContentHost(routeContentHostVM, modifier = Modifier.padding(paddingValues))
            }

            if (appViewModel.isMaskContent) {
                IconButton(
                    enabled = false, onClick = { /*TODO*/ }, modifier = Modifier.fillMaxSize()
                ) {}
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
