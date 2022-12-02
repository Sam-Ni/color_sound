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


@Composable
fun ColorSoundApp() {
    ColorSoundTheme {
        ColorSoundAppEntry()
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ColorSoundAppEntry() {
    val recordService = Injecter.getService<RecordService>()
    val localSoundListService = Injecter.getService<LocalSoundListService>()
    val playSoundService = Injecter.getService<PlaySoundService>()
    val worldService = Injecter.getService<WorldService>()
    val upLoadSoundService = Injecter.getService<UpLoadSoundService>()
    val settingService = Injecter.getService<SettingService>()
    val remoteSoundListService = Injecter.getService<RemoteSoundListService>()

    val saveSoundDialogData by Injecter.getMutable<SaveSoundDialogData>().collectAsState()
    val localSoundListData by Injecter.getMutable<LocalSoundListData>().collectAsState()
    val worldData by Injecter.getMutable<WorldData>().collectAsState()
    val recordData by Injecter.getMutable<RecordData>().collectAsState()
    val searchBarData by Injecter.getMutable<SearchBarData>().collectAsState()
    val maskData by Injecter.getMutable<MaskData>().collectAsState()
    val playSoundData by Injecter.getMutable<PlaySoundData>().collectAsState()
    val worldColorData by Injecter.getMutable<WorldColorData>().collectAsState()
    val configData by Injecter.getMutable<ConfigData>().collectAsState()
    val highlightData by Injecter.getMutable<HighlightData>().collectAsState()
    val onPushResultData by Injecter.getMutable<OnPushResultData>().collectAsState()

    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val askPermission by lazy {
        {
            audioPermissionState.launchPermissionRequest()
        }
    }
    val isGranted = audioPermissionState.status.isGranted


    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        MaterialTheme.colorScheme.background,
        darkIcons = !isSystemInDarkTheme()
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
        isHighlightMode = highlightData.highlightMode,
        onDelete = {
            playSoundData.currentPlayingSound?.let { playSoundService.stopPlayIfSoundIs(it) }
            localSoundListService.onDelete()
            exitHighlight()
        },
        onPush = upLoadSoundService::uploadSound,
        onPushResult = onPushResultData.result,
        onUpdate = recordService::showDialogWithNameAndColor,
        exitHighlight = exitHighlight,
        isPlaying = playSoundData.currentPlayingSound != null,
        onLoop = { highlightData.highlightSound?.let { playSoundService.loopPlay(it) } },
        setUploadIdle = upLoadSoundService::setUploadIdle,
    )
    val coroutineScope = rememberCoroutineScope()
    val homeScreenVM = HomeScreenVM(
        onCardClick = { playSoundService.playOrPause(it) },
        onCardLongClick = { sound: Sound ->
            playSoundService.stopPlayIfSoundIs(sound)
            localSoundListService.enterHighlight(sound)
        },
        onSaveDialogSaveBtnClick = {
            if (!highlightData.highlightMode) {
                localSoundListService.scrollToTop(coroutineScope)
            }
            recordService.onSaveClick()
        },
        onSaveDialogCancelBtnClick = recordService::onCancelClick,
        onSaveDialogSoundNameChanged = recordService::updateSaveName,
        saveDialogChooseSoundColor = recordService::updateChoice,
        onSearchBarValueChanged = localSoundListService::updateSearch,
        searchBarText = searchBarData.search,
        soundListState = localSoundListData.listState,
        soundList = localSoundListData.soundList.filter { it.name.contains(searchBarData.search) },
        currentHighlightSound = highlightData.highlightSound,
        isShowSaveDialog = saveSoundDialogData.showSaveDialog,
        saveDialogSoundNameText = saveSoundDialogData.saveName,
        saveDialogChosenColor = saveSoundDialogData.color,
        currentPlayingSound = playSoundData.currentPlayingSound,
        isPlayingPaused = playSoundData.isPaused,
        isPreparing = playSoundData.isPreparing,
    )

    val worldScreenVM = WorldScreenVM(
        worldNetState = worldData.worldNetState,
        retryAction = {
            worldService.scrollToTop(coroutineScope)
            worldService.getRandomSounds()
        },
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
        soundList = worldData.soundList,
        isPreparing = playSoundData.isPreparing,
    )
    val settingScreenVM = SettingsScreenVM(
        configData.isRepeatPlay, settingService::onIsRepeatPlayChanged,
        settingService::onLanguageSelect,
        language = configData.language,
        isBackPlay = configData.backgroundPlay,
        onIsBackPlayChanged = { settingService.onIsBackPlayChanged(it) },
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

        Mask(isMask = maskData.isMask)
    }

}

@Composable
fun Mask(isMask: Boolean) {
    if (isMask) { //Mask
        IconButton(
            enabled = false, onClick = { /*TODO*/ }, modifier = Modifier.fillMaxSize()
        ) {}
    }
}
