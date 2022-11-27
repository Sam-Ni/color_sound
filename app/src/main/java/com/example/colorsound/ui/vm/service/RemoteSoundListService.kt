package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.HighlightData
import com.example.colorsound.ui.vm.data.MaskData
import com.example.colorsound.ui.vm.data.SearchBarData
import com.example.colorsound.ui.vm.data.WorldData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RemoteSoundListService(
    private val maskData: MutableStateFlow<MaskData>,
    private val highlightData: MutableStateFlow<HighlightData>,
) : ViewModel(){

    private fun updateHighlightMode(mode: Boolean, sound: Sound) {
        highlightData.update { it.copy(highlightMode = mode, highlightSound = sound) }
    }

    fun onCardLongClick(sound: Sound) {
        updateHighlightMode(true, sound)
        maskData.update { it.copy(isMask = true) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ColorSoundApplication)
                val saveSoundDialogData = application.container.saveSoundDialogData
                val localSoundListData = application.container.localSoundListData
                val recordData = application.container.recordData
                val maskData = application.container.maskData
                val searchBarData = application.container.searchBarData
                val worldData = application.container.worldData
                val repository = application.container.databaseRepository
                val highlightData = application.container.highlightData
                RemoteSoundListService(maskData, highlightData)
            }
        }
    }
}