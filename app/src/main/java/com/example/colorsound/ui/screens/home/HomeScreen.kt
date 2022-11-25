package com.example.colorsound.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.SaveDialog
import com.example.colorsound.ui.components.SearchBar
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    homeViewModel: HomeViewModel,
) {
    val uiState by homeViewModel.uiState.collectAsState()

    HomeScreen(
        onClickStartPlay = onClickStartPlay,
        soundList = soundList,
        uiState = uiState,
        onSaveClick = homeViewModel::onSaveClick,
        onCancelClick = homeViewModel::onCancelClick,
        onNameChanged = homeViewModel::updateSaveName,
        chooseColor = homeViewModel::updateChoice,
        onSearchValueChanged = homeViewModel::updateSearch,
    )
}


@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    uiState: HomeUiState,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    chooseColor: (Int) -> Unit,
    onSearchValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.showSaveDialog) {
        SaveDialog(
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick,
            onNameChanged = onNameChanged,
            uiState = uiState,
            chooseColor = chooseColor,
        )
    }

    Column(
        modifier = modifier
    ) {
        SearchBar(
            "Search...",
            text = uiState.search,
            onValueChange = onSearchValueChanged,
            onDeleteBtnClick = { onSearchValueChanged("") },
        )
        SoundList(soundList = soundList, onClickStartPlay = onClickStartPlay)
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ColorSoundTheme {
        HomeScreen(
            onClickStartPlay = { _, _ -> },
            soundList = DataSource.soundList,
            uiState = HomeUiState(),
            onSaveClick = {},
            onCancelClick = { /*TODO*/ },
            onNameChanged = {},
            chooseColor = {},
            onSearchValueChanged = {},
        )
    }
}
