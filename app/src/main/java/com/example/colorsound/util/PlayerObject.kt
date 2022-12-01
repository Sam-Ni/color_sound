package com.example.colorsound.util

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class PlayerObject(context: Context) : IPoolable {
    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    override val onInit: () -> Unit
        get() = {
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_IDLE -> {}
                        Player.STATE_BUFFERING -> {}
                        Player.STATE_READY -> {}
                        Player.STATE_ENDED -> {}
                    }
                }
            })
        }
    override val onGet: () -> Unit
        get() = { }
    override val onRelease: () -> Unit
        get() = {
            player.stop()
        }
}
