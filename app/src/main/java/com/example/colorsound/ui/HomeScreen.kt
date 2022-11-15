package com.example.colorsound.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.data.DataSource
import com.example.colorsound.data.SoundInfo
import com.example.colorsound.ui.theme.ColorSoundTheme

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
    soundList: List<SoundInfo>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        SoundList(soundList = soundList)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SaveDialog(
    onSaveClick: () -> Unit,
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
                            onClick = onSaveClick,
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
        SaveDialog({}, {})
    }
}

@Composable
fun ColorSoundFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ColorSoundTheme {
        HomeScreen(soundList = DataSource.soundList)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    ColorSoundTheme {
        SearchBar()
    }
}