package com.example.colorsound.ui.screens.world

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.model.Sound
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WorldUiState {
    data class Success(val sounds: List<Sound>) : WorldUiState
    object Error : WorldUiState
    object Loading : WorldUiState
}

class WorldViewModel(private val colorSoundRepository: RemoteRepository) : ViewModel() {
    var worldUiState: WorldUiState by mutableStateOf(WorldUiState.Loading)
        private set


    init {
        getRandomSounds()
    }


    fun getRandomSounds() {
        viewModelScope.launch {
            worldUiState = WorldUiState.Loading
            worldUiState = try {
                WorldUiState.Success(colorSoundRepository.getRandomSounds())
            } catch (e: IOException) {
                Log.e("ColorSound" ,e.toString())
                WorldUiState.Error
            } catch (e: HttpException) {
                Log.e("ColorSound" ,e.toString())
                WorldUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ColorSoundApplication)
                val colorSoundRepository = application.container.networkRepository
                WorldViewModel(colorSoundRepository = colorSoundRepository)
            }
        }
    }
}