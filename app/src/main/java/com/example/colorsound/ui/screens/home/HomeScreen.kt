package com.example.colorsound.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.*
//import com.example.colorsound.ui.screens.AppUiState
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.SoundInfoFactory




@Composable
fun HomeScreen(
    homeScreenVM: HomeScreenVM,
    modifier: Modifier = Modifier,
) {
    homeScreenVM.apply {
        val searchBarVM = SearchBarVM(
            "Search...",
            text = search,
            onValueChange = onSearchValueChanged,
            onDeleteBtnClick = { onSearchValueChanged("") },
            )
        val soundCardListVM = SoundCardListVM(
            listState = listState,
            soundList = soundList,
            onPlayOrPause = onPlayOrPause,
            onLongClick = onCardLongClick,
            highlightSound = highlightSound,
            )
        val saveDialogVM = SaveDialogVM(
            onSaveClick,
            onCancelClick,
            onNameChanged,
            saveName,
            color,
            chooseColor
        )
        if (showSaveDialog) {
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


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    ColorSoundTheme {
//        HomeScreen(
//            HomeScreenVM(
//                onPlayOrPause = { _, _ -> },
//                onCardLongClick = {},
//                onSaveClick = {},
//                onCancelClick = { /*TODO*/ },
//                onNameChanged = {},
//                chooseColor = {},
//                onSearchValueChanged = {},
//            )
//        )
//    }
//}


data class HomeScreenVM(
    val onPlayOrPause: (String, Int) -> Unit,
    val onCardLongClick: (Sound) -> Unit,
    val onSaveClick: () -> Unit,
    val onCancelClick: () -> Unit,
    val onNameChanged: (String) -> Unit,
    val chooseColor: (Int) -> Unit,
    val onSearchValueChanged: (String) -> Unit,
    val search: String,
    val listState: LazyListState,
    val soundList: List<Sound>,
    val highlightSound: Sound?,
    val showSaveDialog: Boolean,
    val saveName: String,
    val color: Int,
)