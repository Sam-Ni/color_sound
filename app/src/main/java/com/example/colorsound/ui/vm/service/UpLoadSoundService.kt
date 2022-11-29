package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.ui.vm.data.HighlightData
import com.example.colorsound.ui.vm.data.MaskData
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpLoadSoundService : ViewModel() {
    private val networkRepository = Injecter.get<RemoteRepository>()
    private val maskData = Injecter.getMutable<MaskData>()
    private val highlightData = Injecter.getMutable<HighlightData>()

    fun uploadSound() {
        viewModelScope.launch {
            highlightData.value.highlightSound?.let { networkRepository.uploadSound(it) }
            highlightData.update { it.copy(highlightSound = null, highlightMode = false) }
            maskData.update { it.copy(isMask = false) }
        }
    }
}