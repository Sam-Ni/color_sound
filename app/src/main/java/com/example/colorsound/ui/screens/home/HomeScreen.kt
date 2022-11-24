package com.example.colorsound.ui.screens.home

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.R
import com.example.colorsound.data.DataSource
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.ButtonWithLongPress
import com.example.colorsound.util.COLORS
import com.example.colorsound.util.COLOR_NUMBER
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .heightIn(56.dp)
        ,
        leadingIcon ={
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        placeholder = {
            Text(text = "Search")
        },
//        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Blue)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    homeViewModel: HomeViewModel,
    isGranted: Boolean,
    askPermission: () -> Unit,
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
        isGranted = isGranted,
        askPermission = askPermission,
        onNameChanged = homeViewModel::updateSaveName,
        chooseColor = homeViewModel::updateChoice,
    )
}

@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    uiState: HomeUiState,
    onRecordClick: () -> Unit,
    onRecordLongClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    isGranted: Boolean,
    askPermission: () -> Unit,
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

    Scaffold(
        floatingActionButton = {
            ColorSoundFAB(
                onClick = onRecordClick,
                onLongClick = onRecordLongClick,
                recordState = uiState.recordState,
                isGranted = isGranted,
                askPermission = askPermission,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(Modifier.padding(horizontal = 16.dp))
            SoundList(soundList = soundList, onClickStartPlay = onClickStartPlay)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SaveDialog(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    uiState: HomeUiState,
    chooseColor: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = uiState.saveName,
                        onValueChange = { onNameChanged(it) },
                        placeholder = {
                            Text(text = "Name")
                        }
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
                                    Image(painter = painterResource(id = R.drawable.circle_orange), contentDescription = null)
                                } else { // not chosen state
                                    Image(painter = painterResource(id = R.drawable.circle_black), contentDescription = null)
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
        SaveDialog(onSaveClick = {}, onCancelClick = { /*TODO*/ }, onNameChanged = {}, uiState = HomeUiState(), chooseColor = {})
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun ColorSoundFAB(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    recordState: RecordState,
    isGranted: Boolean,
    askPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonWithLongPress(
        onClick = if (isGranted) onClick else  askPermission ,
        onLongClick = if (isGranted) onLongClick else askPermission ,
    ) {
        when (recordState) {
            RecordState.Normal -> Icon(Icons.Filled.Add, contentDescription = null)
            RecordState.Recording -> Icon(Icons.Filled.Star, contentDescription = null)
            RecordState.Pausing -> Icon(Icons.Filled.ArrowBack, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ColorSoundTheme {
        HomeScreen(
            onClickStartPlay = {_, _->},
            soundList = DataSource.soundList,
            uiState = HomeUiState(),
            onRecordClick = { /*TODO*/ },
            onRecordLongClick = { /*TODO*/ },
            onSaveClick = {},
            onCancelClick = { /*TODO*/ },
            isGranted = false,
            askPermission = { /*TODO*/ },
            onNameChanged = {},
            chooseColor = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    ColorSoundTheme {
        SearchBar()
    }
}