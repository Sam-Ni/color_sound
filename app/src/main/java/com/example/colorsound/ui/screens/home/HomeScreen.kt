package com.example.colorsound.ui.screens.home

import android.renderscript.ScriptGroup.Input
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.R
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.InputBar
import com.example.colorsound.ui.components.SearchBar
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.ButtonWithLongPress
import com.example.colorsound.util.COLOR_NUMBER
import com.example.colorsound.util.IndexToColor


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
        onRecordClick = homeViewModel::onClick,
        onRecordLongClick = homeViewModel::onLongClick,
        onSaveClick = homeViewModel::onSaveClick,
        onCancelClick = homeViewModel::onCancelClick,
        onNameChanged = homeViewModel::updateSaveName,
        chooseColor = homeViewModel::updateChoice,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    uiState: HomeUiState,
    onRecordClick: () -> Unit,
    onRecordLongClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    chooseColor: (Int) -> Unit,
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
        SearchBar("", {}, {})
        SoundList(soundList = soundList, onClickStartPlay = onClickStartPlay)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveDialog(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    uiState: HomeUiState,
    chooseColor: (Int) -> Unit,
) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InputBar(
                        hint = "Sound Name",
                        text = uiState.saveName,
                        onValueChange = { onNameChanged(it) },
                        onDeleteBtnClick = { onNameChanged("") }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 0 until COLOR_NUMBER) {
                            IconButton(
                                onClick = { chooseColor(i) },
                                modifier = Modifier.weight(1f)
                            ) {
                                if (uiState.color == i) { // chosen state
                                    Image(
                                        painter = painterResource(id = R.drawable.circle),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(
                                            IndexToColor(i)
                                        ),
                                        modifier = Modifier.size(60.dp)
                                    )
                                } else { // not chosen state
                                    Image(
                                        painter = painterResource(id = R.drawable.circle),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(
                                            IndexToColor(i)
                                        ),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancelClick,
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            onClick = { onSaveClick() },
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaveDialogPreview() {
    ColorSoundTheme {
        SaveDialog(
            onSaveClick = {},
            onCancelClick = { /*TODO*/ },
            onNameChanged = {},
            uiState = HomeUiState(),
            chooseColor = {})
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
            onRecordClick = { /*TODO*/ },
            onRecordLongClick = { /*TODO*/ },
            onSaveClick = {},
            onCancelClick = { /*TODO*/ },
            onNameChanged = {},
            chooseColor = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    ColorSoundTheme {
        SearchBar("", "", {}, {})
    }
}