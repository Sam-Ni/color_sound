package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.HighlightData
import com.example.colorsound.ui.vm.data.MaskData
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RemoteSoundListService: ViewModel() {
    private val maskData: MutableStateFlow<MaskData> = Injecter.instance().get("MaskData")
    private val highlightData: MutableStateFlow<HighlightData> =
        Injecter.instance().get("HighlightData")

    private fun updateHighlightMode(mode: Boolean, sound: Sound) {
        highlightData.update { it.copy(highlightMode = mode, highlightSound = sound) }
    }

    fun onCardLongClick(sound: Sound) {
        updateHighlightMode(true, sound)
        maskData.update { it.copy(isMask = true) }
    }
}