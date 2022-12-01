package com.example.colorsound.ui.vm.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorsound.data.remote.RemoteRepository
import com.example.colorsound.ui.vm.data.WorldColorData
import com.example.colorsound.ui.vm.data.WorldData
import com.example.colorsound.ui.vm.data.WorldNetState
import com.example.colorsound.util.BASE_URL
import com.example.colorsound.util.Injecter
import com.example.colorsound.util.URL_NGINX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WorldService : ViewModel() {
    private val worldData = Injecter.getMutable<WorldData>()
    private val networkRepository = Injecter.get<RemoteRepository>()
    private val worldColorData = Injecter.getMutable<WorldColorData>()
    private val playSoundService = Injecter.getService<PlaySoundService>()

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
                            .map { sound -> sound.copy(url = URL_NGINX + sound.url) }

                    val sounds =
                        if (worldData.value.previousColor == worldColorData.value.currentColor) {
                            if (it.soundList.size < 20) {
                                result.plus(it.soundList).distinctBy { sound -> sound.url }
                            } else {
                                it.soundList.subList(10, it.soundList.size)
                                    .forEach { sound -> playSoundService.removeSoundPlayer(sound) }
                                result.plus(it.soundList.subList(0, 10))
                                    .distinctBy { sound -> sound.url }
                            }
                        } else {
                            it.soundList.forEach { sound -> playSoundService.removeSoundPlayer(sound) }
                            result
                        }

                    sounds.forEach { sound -> playSoundService.prepareSoundPlayer(sound) }
                    it.copy(
                        worldNetState = WorldNetState.Success,
                        soundList = sounds,
                        previousColor = worldColorData.value.currentColor
                    )
                } catch (e: IOException) {
                    it.copy(worldNetState = WorldNetState.Error)
                } catch (e: HttpException) {
                    it.copy(worldNetState = WorldNetState.Error)
                }
            }
        }
    }
}