package com.example.colorsound.ui.vm.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.PlaySoundData
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaySoundService : ViewModel() {
    private val playSoundData = Injecter.getMutable<PlaySoundData>()

    private val playSoundMap: MutableMap<Sound, ExoPlayer?> = mutableMapOf()


    fun attachSoundWithPlayer(sound: Sound, exoPlayer: ExoPlayer) {
        playSoundMap[sound] = exoPlayer
        val mediaItem = MediaItem.Builder()
            .setUri(sound.url)
            .setMimeType(MimeTypes.AUDIO_MPEG)
            .build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        /* TODO setOnCompleteListener */
//        Player.
    }

//    fun onEndAction(mediaPlayer: ExoPlayer)

    fun detachSoundWithPlayer(sound: Sound) {
        playSoundMap.remove(sound)
    }

    private fun playSound(sound: Sound, mediaPlayer: ExoPlayer) {
        playSoundData.update { it.copy(currentPlayingSound = sound, currentPlayer = mediaPlayer) }

        viewModelScope.launch {
            playSoundMap[sound]?.play()
        }
    }

    private fun stopCurrentSound(mediaPlayer: ExoPlayer) {
        if (playSoundData.value.currentPlayingSound != null) {
            mediaPlayer.stop()
            playSoundData.update {
                it.copy(currentPlayingSound = null, isPaused = false, currentPlayer = null)
            }
        }
    }

    private fun pausePlaySound(mediaPlayer: ExoPlayer) {
        if (playSoundData.value.currentPlayingSound != null) {
            mediaPlayer.pause()
            playSoundData.update { it.copy(isPaused = true) }
        }
    }

    private fun continuePlaySound(mediaPlayer: ExoPlayer) {
        if (playSoundData.value.currentPlayer != null && playSoundData.value.isPaused) {
            playSoundData.update { it.copy(isPaused = false) }
            viewModelScope.launch {
                mediaPlayer.play()
                playSoundData.update { it.copy(isPaused = false) }
            }
        }
    }

    private fun isPlaying(sound: Sound): Boolean {
        return playSoundData.value.currentPlayingSound == sound
    }

    fun resetToBegin(exoPlayer: ExoPlayer) {
        exoPlayer.seekTo(0)
        exoPlayer.pause()
        playSoundData.update { it.copy(isPaused = true, currentPlayingSound = null) }
    }

    fun stopPlayIfSoundIs(sound: Sound) {
        val mediaPlayer = playSoundMap[sound]
        if (playSoundData.value.currentPlayingSound == sound) {
            mediaPlayer?.stop()
            playSoundData.update { it.copy(currentPlayingSound = null, isPaused = false) }
        }
    }

    fun playOrPause(sound: Sound) {
        val mediaPlayer = playSoundMap[sound]
        val currentPlayer = playSoundData.value.currentPlayer
        if (mediaPlayer != null) {
            if (mediaPlayer == currentPlayer) {
                if (mediaPlayer.isPlaying) {
                    pausePlaySound(mediaPlayer)
                } else {
                    playSoundData.update { it.copy(currentPlayingSound = sound, currentPlayer = mediaPlayer) }
                    continuePlaySound(mediaPlayer)
                }
            } else {
                if (currentPlayer != null) {
                    stopCurrentSound(currentPlayer)
                }
                playSound(sound, mediaPlayer)
            }
        }
    }

    fun restorePlayerConfig() {
//        mediaPlayer.isLooping = playSoundData.value.previousLoopState
    }

    fun loopPlay(sound: Sound) {
//        playSoundData.update {
//            it.copy(previousLoopState = mediaPlayer.isLooping, currentPlayingSound = sound)
//        }
//        viewModelScope.launch {
//            mediaPlayer.apply {
//                reset()
//                isLooping = true
//                setDataSource(sound.url)
//                prepare()
//                start()
//            }
//        }
    }
}