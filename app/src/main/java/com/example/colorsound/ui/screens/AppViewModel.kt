package com.example.colorsound.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorsound.model.Sound
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppUiState(
    val highlightMode: Boolean = false,
    val highlightSound: Sound? = null,
)

class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    var currentPlaying: Int = 0
    var isPaused = false

    private fun _play(url: String) {
        viewModelScope.launch {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
            currentPlaying = -1
        }
    }

    private fun updateHighlightMode(mode: Boolean, sound: Sound) {
        _uiState.update { it.copy(highlightMode = mode, highlightSound = sound) }
    }

    fun onCardLongClick(sound: Sound) {
        exitHighlight()
        updateHighlightMode(true, sound)
    }

    fun exitHighlight() {
        _uiState.update { it.copy(highlightMode = false, highlightSound = null) }
    }

    fun play(url: String, soundId: Int) {
        if (mediaPlayer.isPlaying) {
            if (currentPlaying == soundId) {
                mediaPlayer.pause()
                isPaused = true
                return
            } else {
                mediaPlayer.stop()
                mediaPlayer.reset()
                currentPlaying = soundId
                _play(url)
            }
        } else if (isPaused) {
            isPaused = false
            if (currentPlaying == soundId) {
                viewModelScope.launch {
                    mediaPlayer.start()
                }
            } else {
                mediaPlayer.stop()
                mediaPlayer.reset()
                currentPlaying = soundId
                _play(url)
            }
        } else {
            currentPlaying = soundId
            _play(url)
        }
    }
}