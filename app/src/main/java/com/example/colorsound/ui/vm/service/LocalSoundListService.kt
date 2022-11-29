package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.HighlightData
import com.example.colorsound.ui.vm.data.LocalSoundListData
import com.example.colorsound.ui.vm.data.MaskData
import com.example.colorsound.ui.vm.data.SearchBarData
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class LocalSoundListService : ViewModel() {
    private val repository: LocalRepository = Injecter.instance().get("DatabaseRepository")
    private val maskData: MutableStateFlow<MaskData> = Injecter.instance().get("MaskData")
    private val localSoundListData: MutableStateFlow<LocalSoundListData> =
        Injecter.instance().get("LocalSoundListData")
    private val searchBarData: MutableStateFlow<SearchBarData> =
        Injecter.instance().get("SearchBarData")
    private val highlightData: MutableStateFlow<HighlightData> =
        Injecter.instance().get("HighlightData")

    init {
        freshLocalSoundsList()
    }

    private fun freshLocalSoundsList() {
        viewModelScope.launch {
            localSoundListData.update { it.copy(soundList = repository.getAllSounds()) }
        }
    }

    fun scrollToTop(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            localSoundListData.value.listState.animateScrollToItem(index = 0, scrollOffset = -1)
        }
    }

    fun updateSearch(search: String) {
        searchBarData.update { it.copy(search = search) }
    }

    private fun updateHighlightMode(mode: Boolean, sound: Sound?) {
        highlightData.update { it.copy(highlightMode = mode, highlightSound = sound) }
    }

    fun onCardLongClick(sound: Sound) {
        updateHighlightMode(true, sound)
        maskData.update { it.copy(isMask = true) }
    }

    fun onDelete() {
        viewModelScope.launch {
            highlightData.value.highlightSound?.let {
                repository.deleteSound(it)
                deleteAudio(it.url)
                val original = localSoundListData.value.soundList.toMutableList()
                original.remove(it)
                exitHighlight()
                updateSoundList(original)
            }
            maskData.update { it.copy(isMask = false) }
        }
    }

    private fun updateSoundList(soundList: List<Sound>) {
        localSoundListData.update { it.copy(soundList = soundList) }
    }

    fun exitHighlight() {
        updateHighlightMode(false, null)
        maskData.update { it.copy(isMask = false) }
    }

    private fun deleteAudio(fileUrl: String) {
        val file = File(fileUrl)
        file.delete()
    }
}