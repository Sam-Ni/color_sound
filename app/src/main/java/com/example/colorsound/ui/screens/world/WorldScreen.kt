package com.example.colorsound.ui.screens.world

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.colorsound.ui.components.SoundCardListVM
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.vm.data.WorldNetState
import com.example.colorsound.util.BASE_URL

@Composable
fun WorldScreen(
    worldScreenVM: WorldScreenVM
) {
    worldScreenVM.apply {
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
                SoundList(soundCardListVM)
            }
            is WorldNetState.Error -> ErrorScreen(retryAction = retryAction)
        }
    }
}

data class WorldScreenVM(
    val onPlayOrPause: (String, Int) -> Unit,
    val worldNetState: WorldNetState,
    val retryAction: () -> Unit,
)

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Loading")
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
