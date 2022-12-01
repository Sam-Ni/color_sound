package com.example.colorsound.ui.vm.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.ui.vm.data.WorldColorData
import com.example.colorsound.ui.vm.data.WorldData
import com.example.colorsound.ui.vm.data.WorldNetState
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WorldService : ViewModel() {
    private val worldData = Injecter.getMutable<WorldData>()
    private val networkRepository = Injecter.get<RemoteRepository>()
    private val worldColorData = Injecter.getMutable<WorldColorData>()

    init {
        getRandomSounds()
    }

    fun updateChoice(color: Int) {
        worldColorData.update { it.copy(currentColor = color) }
    }

    fun scrollToTop(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            worldData.value.listState.animateScrollToItem(index = 0, scrollOffset = -1)
        }
    }

    /**
     * Refresh Sounds and update UI
     */
    fun getRandomSounds() {
        worldData.update { it.copy(worldNetState = WorldNetState.Loading) }

        viewModelScope.launch {
            worldData.update {
                try {
                    val result =
                        networkRepository.getRandomSounds(worldColorData.value.currentColor)
                    val sounds = if (it.soundsBuffer.size < 20) {
                        result.plus(it.soundsBuffer).distinctBy { sound -> sound.url }
                    } else {
                        result.plus(it.soundsBuffer.subList(0, 10))
                            .distinctBy { sound -> sound.url }
                    }
                    it.copy(
                        worldNetState = WorldNetState.Success(sounds), soundsBuffer = sounds
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
}