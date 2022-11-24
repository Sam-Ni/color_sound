package com.example.colorsound.ui.screens.home

import android.Manifest
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.model.Sound
import com.example.colorsound.ui.components.SoundList
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.ButtonWithLongPress
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
        }
    )
}

@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()

    HomeScreen(
        onClickStartPlay = onClickStartPlay,
        soundList = soundList,
        uiState = uiState,
        onRecordClick = homeViewModel::onClick,
        onRecordLongClick = homeViewModel::onLongClick,
        onSaveClick = homeViewModel::onSaveClick,
        onCancelClick = homeViewModel::onCancelClick)
}

@Composable
fun HomeScreen(
    onClickStartPlay: (String, Int) -> Unit,
    soundList: List<Sound>,
    uiState: HomeUiState,
    onRecordClick: () -> Unit,
    onRecordLongClick: () -> Unit,
    onSaveClick: (Sound) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.showSaveDialog) {
        SaveDialog(
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick,
        )
    }

    Scaffold(
        floatingActionButton = {
            ColorSoundFAB(
                onClick = onRecordClick,
                onLongClick = onRecordLongClick,
                recordState = uiState.recordState
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
    onSaveClick: (Sound) -> Unit,
    onCancelClick: () -> Unit,
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
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text(text = "Name")
                        }
                    )
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text(text = "Color")
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancelClick,
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            onClick = { onSaveClick(Sound(3, 3, "1", "1", "1")) },
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
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
    modifier: Modifier = Modifier
) {
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val askPermission by lazy { {
            audioPermissionState.launchPermissionRequest()
        }
    }
    val isGranted = audioPermissionState.status.isGranted
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
