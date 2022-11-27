package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.HighlightData
import com.example.colorsound.ui.vm.data.LocalSoundListData
import com.example.colorsound.ui.vm.data.MaskData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpLoadSoundService(
    private val networkRepository: RemoteRepository,
    private val maskData : MutableStateFlow<MaskData>,
    private val highlightData: MutableStateFlow<HighlightData>,
) : ViewModel(){
    fun uploadSound() {
        viewModelScope.launch {
            highlightData.value.highlightSound?.let { networkRepository.uploadSound(it) }
            highlightData.update { it.copy(highlightSound = null, highlightMode = false) }
            maskData.update { it.copy(isMask = false) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ColorSoundApplication)
                val networkRepository: RemoteRepository = application.container.networkRepository
                val localSoundListData = application.container.localSoundListData
                val maskData = application.container.maskData
                val highlightData = application.container.highlightData
                UpLoadSoundService(
                    networkRepository, maskData, highlightData
                )
            }
        }
    }
}