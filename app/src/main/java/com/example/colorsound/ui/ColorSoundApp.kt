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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.bottomBar.ScreenBar
import com.example.colorsound.ui.components.bottomBar.ScreenBarVM
import com.example.colorsound.ui.screens.home.HomeScreenVM
import com.example.colorsound.ui.screens.settings.SettingsScreenVM
import com.example.colorsound.ui.screens.world.WorldScreenVM
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.ui.vm.data.*
import com.example.colorsound.ui.vm.service.*
import com.example.colorsound.util.Injecter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundApp() {
    val recordService = RecordService()
    val localSoundListService = LocalSoundListService()
    val playSoundService = PlaySoundService()
    val worldService = WorldService()
    val upLoadSoundService = UpLoadSoundService()
    val settingService = SettingService()
    val remoteSoundListService = RemoteSoundListService()

    val saveSoundDialogData by Injecter.get<MutableStateFlow<SaveSoundDialogData>>("SaveSoundDialogData")
            .collectAsState()
    val localSoundListData by Injecter.get<MutableStateFlow<LocalSoundListData>>("LocalSoundListData")
            .collectAsState()
    val worldData by Injecter.get<MutableStateFlow<WorldData>>("WorldData").collectAsState()
    val recordData by Injecter.get<MutableStateFlow<RecordData>>("RecordData").collectAsState()
    val searchBarData by Injecter.get<MutableStateFlow<SearchBarData>>("SearchBarData")
            .collectAsState()
    val maskData by Injecter.get<MutableStateFlow<MaskData>>("MaskData").collectAsState()
    val playSoundData by Injecter.get<MutableStateFlow<PlaySoundData>>("PlaySoundData")
            .collectAsState()
    val worldColorData by Injecter.get<MutableStateFlow<WorldColorData>>("WorldColorData")
            .collectAsState()
    val configData by Injecter.get<MutableStateFlow<ConfigData>>("ConfigData").collectAsState()
    val highlightData by Injecter.get<MutableStateFlow<HighlightData>>("HighlightData")
            .collectAsState()

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

        val exitHighlight = {
            localSoundListService.exitHighlight()
            playSoundService.restorePlayerConfig()
        }
        val screenBarVM = ScreenBarVM(allScreen = colorSoundTabRowScreens,
                onTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = currentScreen,
                onClick = recordService::onRecordBtnClick,
                onLongClick = recordService::onRecordBtnLongClick,
                recordState = recordData.recordState,
                isGranted = isGranted,
                askPermission = askPermission,
                isHighlightMode = highlightData.highlightMode,
                onDelete = {
                    playSoundData.currentPlayingSound?.let { playSoundService.stopPlayIfSoundIs(it) }
                    localSoundListService.onDelete()
                    exitHighlight()
                },
                onPush = upLoadSoundService::uploadSound,
                onUpdate = {},
                exitHighlight = exitHighlight,
                isPlaying = playSoundData.currentPlayingSound != null,
                onLoop = { highlightData.highlightSound?.let { playSoundService.loopPlay(it) } })
        val coroutineScope = rememberCoroutineScope()
        val homeScreenVM = HomeScreenVM(
                onCardClick = { playSoundService.playOrPause(it) },
                onCardLongClick = { sound: Sound ->
                    playSoundService.stopPlayIfSoundIs(sound)
                    localSoundListService.onCardLongClick(sound)
                },
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
                currentHighlightSound = highlightData.highlightSound,
                isShowSaveDialog = saveSoundDialogData.showSaveDialog,
                saveDialogSoundNameText = saveSoundDialogData.saveName,
                saveDialogChosenColor = saveSoundDialogData.color,
                currentPlayingSound = playSoundData.currentPlayingSound,
                isPlayingPaused = playSoundData.isPaused,
        )

        val worldScreenVM = WorldScreenVM(
                worldNetState = worldData.worldNetState,
                retryAction = worldService::getRandomSounds,
                onPlayOrPause = playSoundService::playOrPause,
                playingSound = playSoundData.currentPlayingSound,
                currentColor = worldColorData.currentColor,
                chooseColor = { worldService.updateChoice(it) },
                listState = worldData.listState,
                isPlayingPaused = playSoundData.isPaused,
                onCardLongClick = {
                    playSoundService.stopPlayIfSoundIs(it)
                    remoteSoundListService.onCardLongClick(it)
                },
                highlightSound = highlightData.highlightSound,
        )
        val settingScreenVM = SettingsScreenVM(
                configData.isRepeatPlay, settingService::onIsRepeatPlayChanged
        )
        val routeContentHostVM = RouteContentHostVM(
                navController = navController,
                homeScreenVM = homeScreenVM,
                worldScreenVM = worldScreenVM,
                settingsScreenVM = settingScreenVM
        )

        Scaffold(
                bottomBar = {
                    ScreenBar(screenBarVM)
                },
        ) { paddingValues ->
            Column {
                RouteContentHost(routeContentHostVM, modifier = Modifier.padding(paddingValues))
            }

            if (maskData.isMask) { //Mask
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
