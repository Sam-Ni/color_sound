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
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound


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
                    onClick = onCardClick,
                    onLongClick = onCardLongClick,
                    isHighlight = if (currentHighlightSound != null) item.url == currentHighlightSound.url else false,
                    isPlaying = if (currentPlayingSound != null) item.url == currentPlayingSound.url else false,
                    isPlayingPaused = isPlayingPaused,
                    isPreparing = isPreparing[item] == true
                )
                SoundCard(
                    soundCardVM = soundCardVM,
                    modifier = Modifier.animateItemPlacement(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
                )
            }
        }

    }
}


data class SoundCardListVM(
    val listState: LazyListState,
    val soundList: List<Sound> = DataSource.soundList,
    val onCardClick: (Sound) -> Unit,
    val onCardLongClick: (Sound) -> Unit,
    val currentHighlightSound: Sound?,
    val currentPlayingSound: Sound?,
    val isPlayingPaused: Boolean,
    val isPreparing: Map<Sound, Boolean>,
)