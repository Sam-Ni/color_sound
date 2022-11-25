package com.example.colorsound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun SoundList(
    soundList: List<Sound>,
    onClickStartPlay: (String, Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {

        items(soundList) { item ->
            SoundCard(soundInfo = item, onClickStartPlay = onClickStartPlay)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SoundListPreview() {
    ColorSoundTheme {
        SoundList(soundList = DataSource.soundList, onClickStartPlay = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun DarkSoundListPreview() {
    ColorSoundTheme(darkTheme = true) {
        SoundList(soundList = DataSource.soundList, onClickStartPlay = { _, _ -> })
    }
}