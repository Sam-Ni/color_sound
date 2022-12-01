package com.example.colorsound.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.colorsound.R
import com.example.colorsound.ui.theme.ColorSoundTheme


@Composable
fun SaveDialog(
    saveDialogVM: SaveDialogVM
) {
    saveDialogVM.apply {
        val inputBarVM =
            InputBarVM(stringResource(R.string.sound_name), saveName, { onNameChanged(it) }, { onNameChanged("") })
        val modifierButtonVM = ModifierButtonVM(text = stringResource(R.string.save), onClick = onSaveClick)
        val modifierOutlinedButtonVM = ModifierButtonVM(text = stringResource(R.string.cancel), onClick = onCancelClick)

        Dialog(onDismissRequest = { /*TODO*/ }) {
            Surface(
                color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InputBar(inputBarVM)

                        ColorChooseRow(
                            ColorChooseRowVM(
                                currentColor = color,
                                chooseColor = chooseColor
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ModifierOutlinedButton(modifierOutlinedButtonVM)
                            ModifierButton(modifierButtonVM)
                        }
                    }
                }
            }
        }
    }
}

data class SaveDialogVM(
    val onSaveClick: () -> Unit,
    val onCancelClick: () -> Unit,
    val onNameChanged: (String) -> Unit,
    val saveName: String,
    val color: Int,
    val chooseColor: (Int) -> Unit,
)

@Preview(showBackground = true)
@Composable
fun SaveDialogPreview() {
    ColorSoundTheme {
        SaveDialog(
            SaveDialogVM(onSaveClick = {},
                onCancelClick = { /*TODO*/ },
                onNameChanged = {},
                saveName = "",
                chooseColor = {},
                color = 1
            )
        )
    }
}