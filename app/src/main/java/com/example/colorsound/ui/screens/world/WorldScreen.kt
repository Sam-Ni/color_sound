package com.example.colorsound.ui.screens.world

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.colorsound.R
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.ColorChooseRow
import com.example.colorsound.ui.components.ColorChooseRowVM
import com.example.colorsound.ui.components.SoundCardListVM
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.vm.data.WorldNetState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun WorldScreen(
    worldScreenVM: WorldScreenVM
) {
    worldScreenVM.apply {
        val colorChooseRowVM = ColorChooseRowVM(currentColor, chooseColor, canCancelSelected = true)
        val loadContentVM =
            LoadContentVM(
                onPlayOrPause,
                worldNetState,
                retryAction,
                playingSound,
                isPlayingPaused,
                listState,
                onCardLongClick,
                highlightSound,
                soundList,
                isPreparing
            )

        Column {
            ColorChooseRow(colorChooseRowVM, modifier = Modifier.padding(20.dp))
            LoadContent(loadContentVM = loadContentVM)
        }
    }
}

@Composable
fun LoadContent(
    loadContentVM: LoadContentVM
) {
    loadContentVM.apply {
        when (worldNetState) {
            is WorldNetState.Loading -> LoadingScreen()
            is WorldNetState.Success -> {
                val sounds = soundList
                val soundCardListVM = SoundCardListVM(
                    listState = listState,
                    soundList = sounds,
                    onCardClick = onPlayOrPause,
                    onCardLongClick = onCardLongClick,
                    currentHighlightSound = highlightSound,
                    currentPlayingSound = playingSound,
                    isPlayingPaused = isPlayingPaused,
                    isPreparing = isPreparing
                )
                SwipeRefresh(
                    state = rememberSwipeRefreshState(worldNetState == WorldNetState.Loading),
                    onRefresh = retryAction
                ) {
                    SoundList(soundCardListVM)
                }
            }
            is WorldNetState.Error -> ErrorScreen(retryAction = retryAction)
        }
    }
}

data class LoadContentVM(
    val onPlayOrPause: (Sound) -> Unit,
    val worldNetState: WorldNetState,
    val retryAction: () -> Unit,
    val playingSound: Sound?,
    val isPlayingPaused: Boolean,
    val listState: LazyListState,
    val onCardLongClick: (Sound) -> Unit,
    val highlightSound: Sound?,
    val soundList: List<Sound>,
    val isPreparing: Map<Sound, Boolean>,
)

data class WorldScreenVM(
    val onPlayOrPause: (Sound) -> Unit,
    val worldNetState: WorldNetState,
    val retryAction: () -> Unit,
    val currentColor: Int,
    val chooseColor: (Int) -> Unit,
    val playingSound: Sound?,
    val isPlayingPaused: Boolean,
    val listState: LazyListState,
    val onCardLongClick: (Sound) -> Unit,
    val highlightSound: Sound?,
    val soundList: List<Sound>,
    val isPreparing: Map<Sound, Boolean>,
)

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ErrorScreen(
    retryAction: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.load_fail))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
