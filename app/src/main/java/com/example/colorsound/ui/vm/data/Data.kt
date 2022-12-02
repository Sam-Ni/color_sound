package com.example.colorsound.ui.vm.data

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.media3.common.Player
import com.example.colorsound.model.Sound

enum class RecordState {
    Recording, Pausing, Normal
}

sealed interface WorldNetState {
    object Success : WorldNetState
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
    val soundList: List<Sound> = emptyList(),
    val listState: LazyListState = LazyListState(),
    val previousColor: Int = -1,
)

data class MaskData(
    val isMask: Boolean = false
)

data class PlaySoundData(
    val currentPlayingSound: Sound? = null,
    val isPaused: Boolean = false,
    val isPreparing: Map<Sound, Boolean> = mapOf(),
    val previousLoopState: Int = Player.REPEAT_MODE_OFF,
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


sealed class Language(
    val name: String,
    val locate: String,
) {
    object Chinese : Language(name = "中文", locate = "zh")
    object English : Language(name = "English", locate = "en")
}
