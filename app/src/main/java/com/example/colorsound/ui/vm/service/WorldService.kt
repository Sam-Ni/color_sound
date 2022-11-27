package com.example.colorsound.ui.vm.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.ui.vm.data.WorldColorData
import com.example.colorsound.ui.vm.data.WorldData
import com.example.colorsound.ui.vm.data.WorldNetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WorldService(
    private val worldData: MutableStateFlow<WorldData>,
    private val networkRepository: RemoteRepository,
    private val worldColorData: MutableStateFlow<WorldColorData>,
) : ViewModel() {


    init {
        getRandomSounds()
    }

    fun updateChoice(color: Int) {
        worldColorData.update { it.copy(currentColor = color) }
    }

    /**
     * Refresh Sounds and update UI
     */
    fun getRandomSounds() {
        worldData.update { it.copy(worldNetState = WorldNetState.Loading) }

        viewModelScope.launch {
            worldData.update {
                try {
                    val result = networkRepository.getRandomSounds(worldColorData.value.currentColor)
                    val sounds = if (it.soundsBuffer.size < 20) {
                        result.plus(it.soundsBuffer).distinctBy { sound ->  sound.url }
                    } else {
                        result.plus(it.soundsBuffer.subList(0, 10)).distinctBy { sound -> sound.url }
                    }
                    it.copy(
                        worldNetState = WorldNetState.Success(sounds),
                        soundsBuffer = sounds
                    )
                } catch (e: IOException) {
                    Log.e("ColorSound", e.toString())
                    it.copy(worldNetState = WorldNetState.Error)
                } catch (e: HttpException) {
                    Log.e("ColorSound", e.toString())
                    it.copy(worldNetState = WorldNetState.Error)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ColorSoundApplication)
                val networkRepository = application.container.networkRepository
                val saveSoundDialogData = application.container.saveSoundDialogData
                val localSoundListData = application.container.localSoundListData
                val recordData = application.container.recordData
                val maskData = application.container.maskData
                val searchBarData = application.container.searchBarData
                val worldData = application.container.worldData
                val worldColorData = application.container.worldColorData
                WorldService(worldData, networkRepository, worldColorData)
            }
        }
    }
}