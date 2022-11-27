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
import com.example.colorsound.ui.screens.home.HomeScreenVM
import com.example.colorsound.ui.screens.world.WorldScreenVM
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.ui.vm.service.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundApp() {
    val getDataService: GetDataService = viewModel(factory = GetDataService.Factory)
    val recordService: RecordService = viewModel(factory = RecordService.Factory)
    val localSoundListService: LocalSoundListService =
        viewModel(factory = LocalSoundListService.Factory)
    val playSoundService = PlaySoundService()
    val worldService: WorldService = viewModel(factory = WorldService.Factory)
    val upLoadSoundService: UpLoadSoundService = viewModel(factory = UpLoadSoundService.Factory)

    val saveSoundDialogData by getDataService.saveSoundDialogData.collectAsState()
    val localSoundListData by getDataService.localSoundListData.collectAsState()
    val worldData by getDataService.worldData.collectAsState()
    val recordData by getDataService.recordData.collectAsState()
    val searchBarData by getDataService.searchBarData.collectAsState()
    val maskData by getDataService.maskData.collectAsState()
    val worldColorData by getDataService.worldColorData.collectAsState()

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

        val screenBarVM = ScreenBarVM(
            allScreen = colorSoundTabRowScreens,
            onTabSelected = { newScreen ->
                navController.navigateSingleTopTo(newScreen.route)
            },
            currentScreen = currentScreen,
            onClick = recordService::onRecordBtnClick,
            onLongClick = recordService::onRecordBtnLongClick,
            recordState = recordData.recordState,
            isGranted = isGranted,
            askPermission = askPermission,
            isHighlightMode = localSoundListData.highlightMode,
            onDelete = localSoundListService::onDelete,
            onPush = upLoadSoundService::uploadSound,
            onUpdate = {},
            exitHighlight = localSoundListService::exitHighlight
        )
        val coroutineScope = rememberCoroutineScope()
        val routeContentHostVM = RouteContentHostVM(
            navController = navController, homeScreenVM = HomeScreenVM(
                onPlayOrPause = playSoundService::play,
                onCardLongClick = { localSoundListService.onCardLongClick(it) },
                onSaveDialogSaveBtnClick = {
                    recordService.onSaveClick()
                    localSoundListService.scrollToTop(coroutineScope)
                },
                onSaveDialogCancelBtnClick = recordService::onCancelClick,
                onSaveDialogSoundNameChanged = recordService::updateSaveName,
                saveDialogChooseSoundColor = recordService::updateChoice,
                onSearchBarValueChanged = localSoundListService::updateSearch,
                searchBarText = searchBarData.search,
                soundListState = localSoundListData.listState,
                soundList = localSoundListData.soundList,
                highlightSound = localSoundListData.highlightSound,
                isShowSaveDialog = saveSoundDialogData.showSaveDialog,
                saveDialogSoundNameText = saveSoundDialogData.saveName,
                saveDialogChosenColor = saveSoundDialogData.color,
            ), worldScreenVM = WorldScreenVM(
                worldNetState = worldData.worldNetState,
                retryAction = worldService::getRandomSounds,
                onPlayOrPause = playSoundService::play,
                currentColor = worldColorData.currentColor,
                chooseColor = { worldService.updateChoice(it) }
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

            if (maskData.isMask) {
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
