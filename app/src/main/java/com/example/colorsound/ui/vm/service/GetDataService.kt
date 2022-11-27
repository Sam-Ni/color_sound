package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.ui.vm.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GetDataService(
    private val _saveSoundDialogData: MutableStateFlow<SaveSoundDialogData>,
    private val _localSoundListData: MutableStateFlow<LocalSoundListData>,
    private val _worldData: MutableStateFlow<WorldData>,
    private val _recordData: MutableStateFlow<RecordData>,
    private val _searchBarData: MutableStateFlow<SearchBarData>,
    private val _maskData: MutableStateFlow<MaskData>,
    private val _playSoundData: MutableStateFlow<PlaySoundData>,
    private val _worldColorData: MutableStateFlow<WorldColorData>,
    private val _config: MutableStateFlow<ConfigData>,
    private val _highlightData: MutableStateFlow<HighlightData>
) : ViewModel() {

    val saveSoundDialogData = _saveSoundDialogData.asStateFlow()
    val localSoundListData = _localSoundListData.asStateFlow()
    val worldData = _worldData.asStateFlow()
    val recordData = _recordData.asStateFlow()
    val searchBarData = _searchBarData.asStateFlow()
    val maskData = _maskData.asStateFlow()
    val playSoundData = _playSoundData.asStateFlow()
    val worldColorData = _worldColorData.asStateFlow()
    val config = _config.asStateFlow()
    val highlightData = _highlightData.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ColorSoundApplication)
                val saveSoundDialogData = application.container.saveSoundDialogData
                val localSoundListData = application.container.localSoundListData
                val recordData = application.container.recordData
                val maskData = application.container.maskData
                val searchBarData = application.container.searchBarData
                val worldData = application.container.worldData
                val playSoundData = application.container.playSoundData
                val worldColorData = application.container.worldColorData
                val configData = application.container.configData
                val highlightData = application.container.highlightData
                GetDataService(
                    saveSoundDialogData,
                    localSoundListData,
                    worldData,
                    recordData,
                    searchBarData,
                    maskData,
                    playSoundData,
                    worldColorData,
                    configData,
                    highlightData,
                )
            }
        }
    }
}