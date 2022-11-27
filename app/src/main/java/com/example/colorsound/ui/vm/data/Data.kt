package com.example.colorsound.ui.vm.data

import androidx.compose.foundation.lazy.LazyListState
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
    val soundsBuffer: List<Sound> = emptyList()
)

data class MaskData(
    val isMask: Boolean = false
)

data class PlaySoundData(
    val currentPlayingSound: Sound? = null,
    val isPaused: Boolean = false
)

data class WorldColorData(
    val currentColor: Int = 0,
)

