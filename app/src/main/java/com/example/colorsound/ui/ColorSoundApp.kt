package com.example.colorsound.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorsound.data.DataSource
import com.example.colorsound.data.SoundInfo
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory

@Composable
fun ColorSoundApp() {

    var soundState by remember {
        mutableStateOf(0)
    }

    val soundList = remember {
        mutableStateListOf(*DataSource.soundList.toTypedArray())
    }

    if (soundState == 2) {
        /* TODO  */
        SaveDialog(
            onCancelClick = { soundState = 0},
            onSaveClick = {
                soundState = 0
                soundList.add(0, SoundInfoFactory())
            }
        )
    }

    ColorSoundTheme {
        Scaffold(
            bottomBar = { ColorSoundBottomNavigation() },
            floatingActionButton = {
                ColorSoundFAB(
                    /* TODO */
                    onClick = { soundState++ }
                )
            }
        ) { paddingValues ->
            HomeScreen(
                soundList = soundList,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ColorSoundTheme {
        ColorSoundApp()
    }
}
