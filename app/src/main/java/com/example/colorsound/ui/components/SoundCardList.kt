package com.example.colorsound.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
//import com.example.colorsound.ui.screens.AppUiState
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundList(
    listState: LazyListState,
    soundList: List<Sound> = DataSource.soundList,
    onPlayOrPause: (String, Int) -> Unit,
    onLongClick: (Sound) -> Unit,
    highlightSound: Sound?
//    appUiState: AppUiState
) {

    LazyColumn(
        state = listState,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxHeight(),
    ) {
        items(
            items = soundList,
            key = { it.url }
        ) { item ->
            SoundCard(
                soundInfo = item,
                onPlayOrPause = onPlayOrPause,
                onLongClick = onLongClick,
                modifier = Modifier.animateItemPlacement(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                isHighlight = if (highlightSound != null) item.url == highlightSound.url else false
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SoundListPreview() {
    ColorSoundTheme {
        SoundList(
            LazyListState(),
            soundList = DataSource.soundList,
            onPlayOrPause = { _, _ -> },
            onLongClick = {},
            highlightSound = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DarkSoundListPreview() {
    ColorSoundTheme(darkTheme = true) {
        SoundList(
            LazyListState(),
            soundList = DataSource.soundList,
            onPlayOrPause = { _, _ -> },
            onLongClick = {},
            highlightSound = null
        )
    }
}