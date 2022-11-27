package com.example.colorsound.ui.screens.world

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.colorsound.ui.components.ColorChooseRow
import com.example.colorsound.ui.components.ColorChooseRowVM
import com.example.colorsound.ui.components.SoundCardListVM
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.vm.data.WorldNetState
import com.example.colorsound.util.BASE_URL
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun WorldScreen(
    worldScreenVM: WorldScreenVM
) {
    worldScreenVM.apply {
        val colorChooseRowVM = ColorChooseRowVM(currentColor, chooseColor)
        val loadContentVM = LoadContentVM(onPlayOrPause, worldNetState, retryAction)

        Column {
            ColorChooseRow(colorChooseRowVM)
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
                val sounds = worldNetState.sounds.map { sound ->
                    sound.copy(url = BASE_URL + sound.url)
                }
                val soundCardListVM = SoundCardListVM(
                    listState = LazyListState(),
                    soundList = sounds,
                    onPlayOrPause = onPlayOrPause,
                    onLongClick = {},
                    highlightSound = null,
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
    val onPlayOrPause: (String, Int) -> Unit,
    val worldNetState: WorldNetState,
    val retryAction: () -> Unit,
)

data class WorldScreenVM(
    val onPlayOrPause: (String, Int) -> Unit,
    val worldNetState: WorldNetState,
    val retryAction: () -> Unit,
    val currentColor: Int,
    val chooseColor: (Int) -> Unit,
)

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
        ,
    ) {
        CircularProgressIndicator()
    }
}


/* TODO change to pull down to refresh */
@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("loading failed")
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}
