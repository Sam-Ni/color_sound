package com.example.colorsound.ui.vm.data

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.media3.exoplayer.ExoPlayer
import com.example.colorsound.model.Sound

enum class RecordState {
    Recording, Pausing, Normal
}

sealed interface WorldNetState {
    data class Success(val sounds: List<Sound>) : WorldNetState
    object Error : WorldNetState
    object Loading : WorldNetState
}


data class SaveSoundDialogData(
    val showSaveDialog: Boolean = false,
    val saveName: String = "",
    val color: Int = 0,
)

data class LocalSoundListData(
    val soundList: List<Sound> = mutableListOf(),
    val listState: LazyListState = LazyListState(),
)

data class HighlightData(
    val highlightMode: Boolean = false,
    val highlightSound: Sound? = null,
)

data class SearchBarData(
    val search: String = "",
)

data class RecordData(
    val recordState: RecordState = RecordState.Normal
)


data class WorldData(
    val worldNetState: WorldNetState = WorldNetState.Loading,
    val soundsBuffer: List<Sound> = emptyList(),
    val listState: LazyListState = LazyListState(),
)

data class MaskData(
    val isMask: Boolean = false
)

data class PlaySoundData(
    val currentPlayingSound: Sound? = null,
    val isPaused: Boolean = false,
    val previousLoopState: Boolean = false,
    val currentPlayer: ExoPlayer? = null,
)

data class WorldColorData(
    val currentColor: Int = -1,
)

enum class PushState {
    Idle, Success, Failure, Uploading
}

data class OnPushResultData(
    val result: PushState = PushState.Idle,
)

data class ContextData(
    val context: Context,
)