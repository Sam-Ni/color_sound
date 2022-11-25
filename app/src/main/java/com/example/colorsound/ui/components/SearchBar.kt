package com.example.colorsound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorsound.data.DataSource
import com.example.colorsound.ui.screens.home.HomeScreen
import com.example.colorsound.ui.screens.home.HomeUiState
import com.example.colorsound.ui.theme.ColorSoundTheme

@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit,
    onDeleteBtnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.headlineLarge.copy(MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorationBox = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "",
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f), contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Search ...",
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        it()
                    }
                    if (text.isNotEmpty()) {
                        IconButton(onClick = onDeleteBtnClick, modifier = Modifier.size(20.dp)) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            },
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .height(50.dp)
                .fillMaxWidth(),
        )
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
