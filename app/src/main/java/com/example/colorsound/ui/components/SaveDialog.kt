package com.example.colorsound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.R
import com.example.colorsound.ui.screens.home.HomeUiState
import com.example.colorsound.ui.theme.ColorSoundTheme
import com.example.colorsound.util.COLOR_NUMBER
import com.example.colorsound.util.IndexToColor


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
                        ModifierOutlinedButton(text = "Cancel", onClick = onCancelClick)
                        ModifierButton(text = "Save", onClick = onSaveClick)
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