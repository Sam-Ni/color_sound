package com.example.colorsound.ui.vm.service

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.PlaySoundData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaySoundService(private val playSoundData: MutableStateFlow<PlaySoundData>) : ViewModel() {
    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
            )
        }
    }

    init {
        mediaPlayer.setOnCompletionListener { onCompletion() }
    }

    private fun onCompletion() {
        playSoundData.update { it.copy(currentPlayingSound = null) }
    }

    private fun playSound(sound: Sound) {
        playSoundData.update { it.copy(currentPlayingSound = sound) }

        viewModelScope.launch {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(sound.url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    private fun stopCurrentSound() {
        if (playSoundData.value.currentPlayingSound != null) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            playSoundData.update { it.copy(currentPlayingSound = null, isPaused = false) }
        }
    }

    private fun pausePlaySound() {
        if (playSoundData.value.currentPlayingSound != null) {
            mediaPlayer.pause()
            playSoundData.update { it.copy(isPaused = true) }
        }
    }

    private fun continuePlaySound() {
        if (playSoundData.value.currentPlayingSound != null && playSoundData.value.isPaused) {
            playSoundData.update { it.copy(isPaused = false) }
            viewModelScope.launch {
                mediaPlayer.start()
                playSoundData.update { it.copy(isPaused = false) }
            }
        }
    }

    private fun isPlaying(sound: Sound): Boolean {
        return playSoundData.value.currentPlayingSound == sound
    }

    fun stopPlayIfSoundIs(sound: Sound) {
        if (playSoundData.value.currentPlayingSound == sound) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            playSoundData.update { it.copy(currentPlayingSound = null, isPaused = false) }
        }
    }

    fun playOrPause(sound: Sound) {
        if (mediaPlayer.isPlaying) { //正在播放
            if (isPlaying(sound)) { //点击的是正在播放的声音
                pausePlaySound() //暂停播放
            } else {
                stopCurrentSound() //暂停播放
                playSound(sound) //播放这个声音
            }
        } else if (playSoundData.value.isPaused) { //正在暂停
            if (isPlaying(sound)) { //如果点击的是当前播放的声音
                continuePlaySound() //继续播放
            } else {
                stopCurrentSound() //暂停原来的声音
                playSound(sound) //播放这个声音
            }
        } else { //什么事都没干
            playSound(sound) //直接播放
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ColorSoundApplication)
                val playSoundData = application.container.playSoundData
                PlaySoundService(playSoundData)
            }
        }
    }


}