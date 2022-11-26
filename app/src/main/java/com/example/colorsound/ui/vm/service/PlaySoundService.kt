package com.example.colorsound.ui.vm.service

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaySoundService : ViewModel() {
    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
            )
        }
    }

    private var currentPlaying: Int = 0
    private var isPaused = false

    private fun _play(url: String) {
        viewModelScope.launch {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
            currentPlaying = -1
        }
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