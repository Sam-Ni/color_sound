package com.example.colorsound.ui.screens.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.*


@Composable
fun HomeScreen(
    homeScreenVM: HomeScreenVM,
    modifier: Modifier = Modifier,
) {
    homeScreenVM.apply {
        val searchBarVM = SearchBarVM(
            hint = "Search...",
            text = searchBarText,
            onValueChange = onSearchBarValueChanged,
            onDeleteBtnClick = { onSearchBarValueChanged("") },
        )
        val soundCardListVM = SoundCardListVM(
            listState = soundListState,
            soundList = soundList,
            onPlayOrPause = onPlayOrPause,
            onLongClick = onCardLongClick,
            highlightSound = highlightSound,
            playingSound = playingSound,
            isPlayingPaused = isPlayingPaused
        )
        val saveDialogVM = SaveDialogVM(
            onSaveClick = onSaveDialogSaveBtnClick,
            onCancelClick = onSaveDialogCancelBtnClick,
            onNameChanged = onSaveDialogSoundNameChanged,
            saveName = saveDialogSoundNameText,
            color = saveDialogChosenColor,
            chooseColor = saveDialogChooseSoundColor
        )
        if (isShowSaveDialog) {
            SaveDialog(saveDialogVM)
        }

        Column(
            modifier = modifier
        ) {
            SearchBar(searchBarVM = searchBarVM)
            SoundList(soundCardListVM = soundCardListVM)
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}


data class HomeScreenVM(
    val onPlayOrPause: (Sound) -> Unit,
    val onCardLongClick: (Sound) -> Unit,

    val onSaveDialogSaveBtnClick: () -> Unit,
    val onSaveDialogCancelBtnClick: () -> Unit,
    val onSaveDialogSoundNameChanged: (String) -> Unit,
    val saveDialogSoundNameText: String,
    val saveDialogChooseSoundColor: (Int) -> Unit,
    val isShowSaveDialog: Boolean,
    val saveDialogChosenColor: Int,

    val onSearchBarValueChanged: (String) -> Unit,
    val searchBarText: String,

    val soundListState: LazyListState,
    val soundList: List<Sound>,
    val highlightSound: Sound?,
    val playingSound: Sound?,
    val isPlayingPaused: Boolean,
)