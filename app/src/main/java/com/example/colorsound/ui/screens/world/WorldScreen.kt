package com.example.colorsound.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.data.DataSource
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.screens.world.WorldUiState
import com.example.colorsound.util.BASE_URL

@Composable
fun WorldScreen(
    onClickStartPlay: (String, Int) -> Unit,
    worldUiState: WorldUiState,
    retryAction: () -> Unit,
) {
    when (worldUiState) {
        is WorldUiState.Loading -> LoadingScreen()
        is WorldUiState.Success -> {
            val sounds = worldUiState.sounds.map { sound ->
                sound.copy(url = BASE_URL + sound.url)
            }
            SoundList(
                soundList = sounds,
                onClickStartPlay = onClickStartPlay
            )
        }
        is WorldUiState.Error -> ErrorScreen(retryAction = retryAction)
    }
}

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

@Preview(showBackground = true)
@Composable
fun WorldScreenPreView() {
//    WorldScreen(
//        worldUiState = WorldUiState.Success(DataSource.soundList),
//        retryAction = { /*TODO*/ },
//        onClickStartPlay = {}
//    )
}