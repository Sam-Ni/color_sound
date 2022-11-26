package com.example.colorsound.ui.vm.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.ui.vm.data.WorldData
import com.example.colorsound.ui.vm.data.WorldNetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WorldService(
    private val worldData: MutableStateFlow<WorldData>,
    private val networkRepository: RemoteRepository
) : ViewModel() {


    init {
        getRandomSounds()
    }


    fun getRandomSounds() {
        viewModelScope.launch {
            worldData.update { it.copy(worldNetState = WorldNetState.Loading) }
            worldData.update {
                it.copy(
                    worldNetState = try {
                        WorldNetState.Success(networkRepository.getRandomSounds())
                    } catch (e: IOException) {
                        Log.e("ColorSound", e.toString())
                        WorldNetState.Error
                    } catch (e: HttpException) {
                        Log.e("ColorSound", e.toString())
                        WorldNetState.Error
                    }
                )
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
                WorldService(worldData, networkRepository)
            }
        }
    }
}