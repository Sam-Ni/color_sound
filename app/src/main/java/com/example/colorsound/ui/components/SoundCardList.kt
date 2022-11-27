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
import com.example.colorsound.ui.theme.ColorSoundTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundList(
    soundCardListVM: SoundCardListVM,
) {
    soundCardListVM.apply {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxHeight(),
        ) {
            items(items = soundList, key = { it.url }) { item ->
                val soundCardVM = SoundCardVM(
                    soundInfo = item,
                    onPlayOrPause = onPlayOrPause,
                    onLongClick = onLongClick,
                    isHighlight = if (highlightSound != null) item.url == highlightSound.url else false,
                    isPlaying = if (playingSound != null) item.url == playingSound.url else false
                )
                SoundCard(
                    soundCardVM = soundCardVM,
                    modifier = Modifier.animateItemPlacement(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SoundListPreview() {
    ColorSoundTheme {
        SoundList(
            SoundCardListVM(LazyListState(),
                soundList = DataSource.soundList,
                onPlayOrPause = { _-> },
                onLongClick = {},
                highlightSound = null,
                playingSound = null
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DarkSoundListPreview() {
    ColorSoundTheme(darkTheme = true) {
        SoundList(
            SoundCardListVM(LazyListState(),
                soundList = DataSource.soundList,
                onPlayOrPause = { _ -> },
                onLongClick = {},
                highlightSound = null,
                playingSound = null
            )
        )
    }
}

data class SoundCardListVM(
    val listState: LazyListState,
    val soundList: List<Sound> = DataSource.soundList,
    val onPlayOrPause: (Sound) -> Unit,
    val onLongClick: (Sound) -> Unit,
    val highlightSound: Sound?,
    val playingSound: Sound?
)