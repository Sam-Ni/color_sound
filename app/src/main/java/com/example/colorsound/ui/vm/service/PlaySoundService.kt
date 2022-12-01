package com.example.colorsound.ui.vm.service

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.vm.data.PlaySoundData
import com.example.colorsound.util.Injecter
import com.example.colorsound.util.ObjectPool
import com.example.colorsound.util.PlayerObject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaySoundService(context: Context) : ViewModel() {
    private val playSoundData = Injecter.getMutable<PlaySoundData>()
    private val playSoundMap: MutableMap<Sound, PlayerObject> = mutableMapOf()
    private val isPreparing: MutableMap<Sound, Boolean> = mutableMapOf()


    private val players: ObjectPool<PlayerObject> =
        ObjectPool(20) { PlayerObject(context = context) }

    fun setRepeatMode(repeatMode: Int) {
        players.forEach { it.player.repeatMode = repeatMode }
    }

    fun prepareSoundPlayer(sound: Sound) {
        if (!playSoundMap.containsKey(sound)) {
            isPreparing[sound] = true
            playSoundData.update { it.copy(isPreparing = isPreparing.toMap()) }

            playSoundMap[sound] = players.get()
            playSoundMap[sound]?.player?.setMediaItem(
                MediaItem.Builder()
                    .setUri(sound.url)
                    .setMimeType(MimeTypes.AUDIO_MPEG)
                    .build()
            )
            playSoundMap[sound]?.player?.addListener(
                object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> {
                                stopCurrentSound()
                            }
                            Player.STATE_READY -> {
                                isPreparing[sound] = false
                                playSoundData.update { it.copy(isPreparing = isPreparing.toMap()) }
                            }
                            else -> {}
                        }
                    }
                }
            )
            playSoundMap[sound]?.player?.prepare()
        }
    }

    fun removeSoundPlayer(sound: Sound) {
        players.release(playSoundMap[sound])
        playSoundMap.remove(sound)

        isPreparing.remove(sound)
        playSoundData.update { it.copy(isPreparing = isPreparing.toMap()) }
    }

    private fun playSound(sound: Sound) {
        viewModelScope.launch {
            if (playSoundMap.containsKey(sound)) {
                if (playSoundMap[sound]?.player?.playbackState == Player.STATE_READY) {
                    playSoundData.update { it.copy(currentPlayingSound = sound) }
                    playSoundMap[sound]?.player?.play()
                }
            } else {
                Log.e("play service", "没有对应的player")
            }
        }
    }

    private fun stopCurrentSound() {
        if (playSoundData.value.currentPlayingSound != null) {
            playSoundMap[playSoundData.value.currentPlayingSound]?.player?.pause()
            playSoundMap[playSoundData.value.currentPlayingSound]?.player?.seekTo(0)

            playSoundData.update {
                it.copy(currentPlayingSound = null, isPaused = false)
            }
        }
    }

    private fun pausePlaySound() {
        if (playSoundData.value.currentPlayingSound != null) {
            playSoundMap[playSoundData.value.currentPlayingSound]?.player?.pause()

            playSoundData.update { it.copy(isPaused = true) }
        }
    }

    private fun continuePlaySound() {
        if (playSoundData.value.isPaused) {
            viewModelScope.launch {
                playSoundMap[playSoundData.value.currentPlayingSound]?.player?.play()
                playSoundData.update { it.copy(isPaused = false) }
            }
        }
    }

    private fun isPlaying(sound: Sound): Boolean {
        return playSoundData.value.currentPlayingSound == sound
    }

    fun stopPlayIfSoundIs(sound: Sound) {
        if (playSoundData.value.currentPlayingSound == sound) {
            stopCurrentSound()
        }
    }

    fun playOrPause(sound: Sound) {
        val currentSound = playSoundData.value.currentPlayingSound
        val isPaused = playSoundData.value.isPaused

        if (sound == currentSound) {
            if (isPaused) {
                continuePlaySound()
            } else {
                pausePlaySound()
            }
        } else {
            stopCurrentSound()
            playSound(sound)
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